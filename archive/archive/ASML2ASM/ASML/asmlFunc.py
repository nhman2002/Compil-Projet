from .asmlBranch import asmlBranch
from .common.operType import operType
from .asmlOper import asmlOper
from .common.asmlFunDef import asmlFunDef
from .asmlIf import asmlIf
import re

class asmlFunc(asmlBranch,asmlFunDef):
    def __init__(self,instruction):
        asmlBranch.__init__(self)
        self.expressions=[]
        self.parameters=[]
        self.allocator=None
        data=instruction.split(" ")
        self.stackcnt=None
        self.name=data[1]
        for i in range(2,len(data)-1):
            optype=None
            if re.match(r'[0-9]+',data[i]) is not None:
                optype=operType.IMM
            else:
                optype=operType.VAR
            op=asmlOper(data[i],optype)
            self.parameters.append(op)
    
    def allocRegSpill(self):
        self.allocator={} 
        
        regcnt=0 
        maxRegcnt=3
        self.stackcnt=8
        for p in self.parameters:
            if regcnt<=maxRegcnt: 
                self.allocator[p.getName()]="r" + str(regcnt)
                p.renameVariable("r" + str(regcnt))
                regcnt+=1
            else: 
                self.allocator[p.getName()]="[fp, #" + str(self.stackcnt) + "]"
                p.renameVariable("[fp, #" + str(self.stackcnt) + "]")
                self.stackcnt+=4
        
        ops=[]
        for exp in self.expressions:
            ops=ops+exp.getOpersCon(operType.VAR)


        varNames=[]
        for op in ops:
            if not op.getName() in varNames and not op.getName() in self.allocator:
                varNames.append(op.getName())
        
        regcnt = 5 
        if len(varNames)<8: 
            maxRegcnt = 10
        else: 
            maxRegcnt = 8
        self.stackcnt = -4

        for  nameOp in varNames: 
            if regcnt <= maxRegcnt:
                self.allocator[nameOp]="r" + str(regcnt)
                regcnt += 1
            else:
                self.stackcnt -=4
                self.allocator[nameOp]="[fp, #" + str(self.stackcnt) + "]"
        
        
        for op in ops:
            
            op.renameVariable(self.allocator[op.getName()])

        
    def generateAsm(self):
        code = ""
        if self.name == "_": #main
            code += "_start:\n"
            code += "\tadd r4, sp, #0\n" #r4 = heap pointer
            code += "\tsub sp, sp, #1000\n" # heap
            code += "\tpush {fp, lr}\n"
            code += "\tadd fp, sp, #4\n"
            code += "\tsub sp, sp, #" + str(abs(8- self.stackcnt)) + "\n" #place for variables that extend + 8 (return + fp)
        else: # other functions
            code += self.name + ":\n"
            code += "\tstr fp, [sp, #-4]\n"
            code += "\tadd fp, sp, #0\n"
            code += "\tsub sp, sp, #" + str(abs(4- self.stackcnt)) + "\n" #place for variables that extend + 4 (fp)

        # code to execute
        for exp in self.expressions:
            #print("st\n",exp,"\nend")
            code += exp.generateAsm()
            if isinstance(exp,asmlIf):
                code += exp.exLabel+":\n"
            

        if not (self.name==("_")):
            #save the registers r5-r10 and r12-r13
            code += "\tpush {r5-r10,r12-r13}\n"
            #restoration of registers
            code += "\tpop {r5-r10,r12-r13}\n"

        #end
        code += "\tldr fp, [sp], #4\n" # restoration of fp

        if self.name==("_"):
            code +="\tbl min_caml_exit\n"
        else:
            code += "\tbx lr\n"

        return code

    def addInstruction(self, expression):
        self.expressions.append(expression)
    
    def __str__(self):
        res = "FUNCTION " + self.name + "\n"
        res += "<parameters> "
        for op in self.parameters:
            res += op.getName() + " "

        res += "\n<code>\n"
        for e in self.expressions:
            res += str(e) + "\n"

        return res