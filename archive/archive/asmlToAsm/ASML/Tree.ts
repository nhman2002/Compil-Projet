import { asmlCall } from './asmlCall';
import { asmlNew } from './asmlNew';
import { asmlMem } from './asmlMem';
import { asmlArithmetics } from './asmlArithmetics';
import { asmlLabel } from './asmlLabel';
import { asmlBranch } from './asmlBranch';
import { asmlFunc } from './asmlFunc';
import { asmlFloat } from './asmlFloat';
import { asmlIf } from './asmlIf';
import { asmlLet } from './asmlLet';
import { asmlFunDef } from './common/asmlFunDef';
import { insType } from './common/insType';

class Tree {
    labels: any[] = [];

    constructor(code: string) {
        const instructions = code.split('\n');
        let currentBranch = new asmlBranch();

        for (const instr of instructions) {
            currentBranch = this.decoderInstruction(instr.trim(), currentBranch);
        }
    }

    private decoderInstruction(instruction: string, currentBranch: any): any {
        const instrType = insType.getTypeInstruction(instruction);

        switch (instrType) {
            case insType.LET_FUN:
                const a_fun = new asmlFunc(instruction);
                this.labels.push(a_fun);
                currentBranch = a_fun;
                break;
            case insType.LET_FLOAT:
                const a_float = new asmlFloat(instruction);
                this.labels.push(a_float);
                break;
            case insType.IF:
                const a_if = new asmlIf(instruction);
                currentBranch.addInstruction(a_if);
                const tmp = currentBranch;
                currentBranch = a_if;
                currentBranch.setParent(tmp);
                break;
            case insType.ELSE:
                currentBranch.setConstructionThen(false);
                break;
            case insType.FI:
                currentBranch = currentBranch.getParent();
                break;
            case insType.LET_IN:
                const a_let = new asmlLet(instruction);
                currentBranch.addInstruction(a_let);
                break;
            case insType.ADD:
            case insType.SUB:
            case insType.FADD:
            case insType.FSUB:
                const a_arith = new asmlArithmetics(instruction);
                currentBranch.addInstruction(a_arith);
                break;
            case insType.CALL:
                const a_call = new asmlCall(instruction);
                currentBranch.addInstruction(a_call);
                break;
            case insType.NOP:
                // Do nothing
                break;
            case insType.MEM:
                const a_mem = new asmlMem(instruction);
                currentBranch.addInstruction(a_mem);
                break;
        }
        return currentBranch;
    }

    registerAllocation_Spill(): void {
        for (const label of this.labels) {
            if (label instanceof asmlFunc) {
                label.allocRegSpill();
            }
        }
    }

    generateAsm(): string {
        let code = "\t.text\n";
        code += "\t.global _start\n";
        for (const a of this.labels) {
            code += a.generateAsm();
        }
        return code;
    }

    toString(): string {
        let res = "-------- TREE ----------\n";
        for (const a of this.labels) {
            res += a.toString();
        }
        return res;
    }
}

export { Tree };
