from .asmlCall import asmlCall
from .asmlMem import asmlMem
from .asmlArithmetics import asmlArithmetics
from .asmlBranch import asmlBranch
from .asmlFunc import asmlFunc
from .asmlFloat import asmlFloat
from .asmlIf import asmlIf
from .asmlLet import asmlLet
from .common.insType import insType


class Tree:
    def __init__(self,code):
        self.labels=[]
        instructions=code.splitlines()
        currentBranch=asmlBranch() 
        
        for instr in instructions:
            currentBranch=self.decoderInstruction(instr,currentBranch)

    def decoderInstruction(self, instruction,currentBranch):
        instruction=instruction.strip()
        instrType=insType.getTypeInstruction(instruction)

        if instrType==insType.LET_FUN:
            a_fun=asmlFunc(instruction)
            self.labels.append(a_fun)
            currentBranch=a_fun
        elif instrType==insType.LET_FLOAT:
            a_float=asmlFloat(instruction)
            self.labels.append(a_float)
        elif instrType==insType.IF:
            a_if=asmlIf(instruction)
            currentBranch.addInstruction(a_if)
            tmp=currentBranch
            currentBranch=a_if
            currentBranch.setParent(tmp)
        elif instrType==insType.ELSE:
            currentBranch.setConstructionThen(False)
        elif instrType==insType.FI:
            currentBranch=currentBranch.getParent()
        elif instrType==insType.LET_IN:
            a_let=asmlLet(instruction)
            currentBranch.addInstruction(a_let)
        elif instrType==insType.ADD:
            a_arith=asmlArithmetics(instruction)
            currentBranch.addInstruction(a_arith)
        elif instrType==insType.SUB:
            a_arith=asmlArithmetics(instruction)
            currentBranch.addInstruction(a_arith)
        elif instrType==insType.FADD:
            a_arith=asmlArithmetics(instruction)
            currentBranch.addInstruction(a_arith)
        elif instrType==insType.FSUB:
            a_arith=asmlArithmetics(instruction)
            currentBranch.addInstruction(a_arith)
        elif instrType==insType.CALL:
            a_call=asmlCall(instruction)
            currentBranch.addInstruction(a_call)
        elif instrType==insType.NOP:
            pass
        elif instrType==insType.MEM:
            a_mem=asmlMem(instruction)
            currentBranch.addInstruction(a_mem)
        return currentBranch

    def registerAllocation_Spill(self):
        for label in self.labels:
            if isinstance(label,asmlFunc):
                #parameters
                label.allocRegSpill()
    
    def generateAsm(self):
        code = ""
        code += "\t.text\n"
        code += "\t.global _start\n"
        for a in self.labels:
            code += a.generateAsm()
        return code
    
    def __str__(self):
        res = "-------- TREE ----------\n"
        for a in self.labels:
            res += str(a)
        return res
