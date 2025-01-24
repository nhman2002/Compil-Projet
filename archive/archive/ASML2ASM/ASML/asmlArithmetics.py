from .asmlExp import asmlExp
from .common.operType import operType
from .asmlOper import asmlOper
import re

class asmlArithmetics(asmlExp):
    def __init__(self, instruction):
        data=instruction.split(" ")
        self.operator=data[0]
        self.op1=asmlOper(data[1],operType.VAR)
        optype=None
        if re.match(r'[0-9]+',data[2]) is not None:
            optype=operType.IMM
        else:
            optype=operType.VAR
        self.op2=asmlOper(data[2],optype)

    def __str__(self):
        return self.operator + " " + str(self.op1) + " " + str(self.op2)

    def getOpers(self):
        a=[]
        a.append(self.op1)
        a.append(self.op2)
        return a

    def getOpersCon(self,type):
        a=[]
        a.append(self.op1)
        if self.op2.getType()==type:
            a.append(self.op2)
        return a

    def generateAsm(self):
        code=""
        if  self.op1.getName().startswith("r") and self.op2.getName().startswith("r"):
            code +="\t" + self.operator + " r12, " + str(self.op1) + ", " + str(self.op2) + "\n"
        else:
            if not self.op1.getName().startswith("r") and not self.op2.getName().startswith("r"):
                if self.op1.isVariable():
                    code += "\tldr r9, " + str(self.op1) + "\n"
                else:
                    code += "\tldr r9, =#" + str(self.op1) + "\n"
                if self.op2.isVariable():
                    code += "\tldr r10, " + str(self.op2) + "\n"
                else:
                    code += "\tldr r10, =#" + str(self.op2) + "\n"
                code += "\t" + self.operator + " r12, r9, r10\n"
            elif not self.op1.getName().startswith("r"):
                if self.op1.isVariable():
                    code += "\tldr r9, " + str(self.op1) + "\n"
                else:
                    code += "\tldr r9, =#" + str(self.op1) + "\n"
                code += "\t" + self.operator + " r12, r10, " + str(self.op2) + "\n"
            else: 
                if self.op2.isVariable():
                    code += "\tldr r10, " + str(self.op2) + "\n"
                else:
                    code += "\tldr r10, =#" + str(self.op2) + "\n"
                code += "\t" + self.operator + " r12, " + str(self.op1) + ", r10\n"
        return code

