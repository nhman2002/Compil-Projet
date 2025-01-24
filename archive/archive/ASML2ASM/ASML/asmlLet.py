from .asmlExp import asmlExp
from .common.operType import operType
from .asmlOper import asmlOper
from .common.insType import insType
from .asmlCall import asmlCall
from .asmlNew import asmlNew
from .asmlMem import asmlMem
from .asmlArithmetics import asmlArithmetics
from .asmlLabel import asmlLabel
import re

class asmlLet(asmlExp):
    def __init__(self,instruction):
        data=instruction.split(" ")
        optype=None
        if re.match(r'[0-9]+',data[1]) is not None:
            optype=operType.IMM
        else:
            optype=operType.VAR
        self.op1=asmlOper(data[1],optype)
        exp=""
        for i in range(3,len(data)-1):
            exp=exp+data[i]+" "
        exp=exp.strip()
        self.op2=None
        instrType=insType.getTypeInstruction(exp)
        if instrType==insType.INT:
            self.op2=asmlOper(exp, operType.IMM)
        elif instrType==insType.IDENT:
            self.op2=asmlOper(exp, operType.VAR)
        elif instrType==insType.CALL:
            self.op2=asmlCall(exp)
        elif instrType==insType.MEM:
            self.op2=asmlMem(exp)
        elif instrType==insType.ADD:
            self.op2=asmlArithmetics(exp)
        elif instrType==insType.SUB:
            self.op2=asmlArithmetics(exp)
        elif instrType==insType.FSUB:
            self.op2=asmlArithmetics(exp)
        elif instrType==insType.FADD:
            self.op2=asmlArithmetics(exp)
        elif instrType==insType.LABEL:
            self.op2=asmlLabel(exp)
        elif instrType==insType.NEW:
            self.op2=asmlNew(exp)
        else:
            print("ERROR : "+ instruction)
        

    def getOpers(self):
        a=[]
        a.append(self.op1)
        a=a+self.op2.getOpers()
        return a

    def getOpersCon(self,type):
        a=[]
        a.append(self.op1)
        a=a+self.op2.getOpersCon(type)
        return a

    def __str__(self):
        return "let " + str(self.op1) + " = " + str(self.op2)

    def generateAsm(self):
        code = ""
        code += self.op2.generateAsm()
        if self.op1.getName().startswith("r"):
            code += "\tmov " + str(self.op1) + ", r12\n"
        else:
            opName = self.op1.getName()
            opName=opName.replace("[", "")
            opName=opName.replace("]","")
            data=opName.split(", ")
            code += "\tstr r12, " + "[fp" + ", " + data[1] + "]\n"

        return code