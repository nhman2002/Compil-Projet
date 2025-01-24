from .asmlExp import asmlExp
from .common.operType import operType
from .asmlOper import asmlOper
import re

class asmlMem(asmlExp):
   
    def __init__(self,instruction):
        data=re.split(r'[ ()]',instruction)
        self.op1=asmlOper(data[1],operType.VAR)
        optype=None
        if re.match(r'[0-9]+',data[3]) is not None:
            optype=operType.IMM
        else:
            optype=operType.VAR
        self.op2=asmlOper(data[3],optype)
        self.op3=None
        if len(data)==7: 
            self.op3=asmlOper(data[6],operType.VAR)
    
    def getOpers(self):
        a=[]
        a.append(self.op1)
        a=a+self.op2.getOpers()
        if self.op3 is not None:
            a.append(self.op3)
        return a

    def getOpersCon(self,type):
        a=[]
        a.append(self.op1)
        a=a+self.op2.getOpersCon(type)
        if self.op3 is not None:
            a.append(self.op3)
        return a
    
    def __str__(self):
        res = "mem(" + str(self.op1) + " + " + str(self.op2) + ")"
        if self.op3 is not None:
            res += " <- " + str(self.op3)
        
        return res

    def generateAsm(self):
        code=""
        if self.op1.getName().startswith("r") and self.op2.getName().startswith("r"):
            code += "\tadd r12, " + str(self.op1) + ", " + str(self.op2) + "\n"
        else:
            if not self.op1.getName().startswith("r") and not self.op2.getName().startswith("r"): 
                if self.op1.isVariable():
                    code += "\tldr r9, " + str(self.op1) + "\n"
                else:
                    code += "\tmov r9, " + str(self.op1) + "\n"
            
                if self.op2.isVariable():
                    code += "\tldr r10, " + str(self.op2) + "\n"
                else:
                    code += "\tldr r10, =#" + str(self.op2) + "\n"
                
                code += "\tadd r12, r9, r10\n"
            elif not self.op1.getName().startswith("r"): 
                if self.op1.isVariable():
                    code += "\tldr r9, " + str(self.op1) + "\n"
                else:
                    code += "\tldr r9, =#" + str(self.op1) + "\n"
                
                code += "\tadd r12, r10, " + str(self.op2) + "\n"
            else: 
                if self.op2.isVariable():
                    code += "\tldr r10, " + str(self.op2) + "\n"
                else:
                    code += "\tldr r10, =#" + str(self.op2) + "\n"
    
                code += "\tadd r12, " + str(self.op1) + ", r10\n"

        
        if self.op3 is not None: 
            if self.op3.getName().startswith("r"): 
                code += "\tldr " + str(self.op3) + ", [r4, r12]\n" 
            else:
                if self.op3.isVariable(): 
                    code += "\tldr r9, "+str(self.op3)+'\n'
                    code += "\tstr r9" + ", [r4, r12]\n" 
                else: 
                    code += "\tstr " + str(self.op3) + ", [r4, r12]\n" 
        return code