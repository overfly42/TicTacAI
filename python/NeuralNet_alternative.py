import torch.nn as nn
import torch
import numpy as np
import torch.optim as optim
import torch.nn.functional as F
from random import random,randrange,sample

device = "cpu"# torch.device("cuda" if torch.cuda.is_available() else "cpu")

class Network(nn.Module):
    def __init__(self,H,n):
        '''
        H is number of Hidden neurons
        n is number of hidden layers (minimum 0)
        '''
        n = n if n >=0 else 0
        super(Network,self).__init__()
        #input vector: field configuration + selected move
        self.modelIn = nn.Linear(9,H).to(device)
        self.hidden = []
        for i in range(n):
            self.hidden.append(nn.Linear(H,H).to(device))
        self.modelOut=nn.Linear(H,9)#.to(device)
        
        self.memory = []
        self.optimizer = optim.RMSprop(self.parameters())
        self.randomValue = 0.075
        self.randomDecay = 0.001
        self.epoch = 10
        self.currentEpoch = 0
        

    
    def forward(self, x):
        '''
        Input of nine values for the current state of the 
        TicTacToe Field
        results in a Tensor 
        '''
        relu = self.modelIn(torch.tensor(x,dtype=torch.float,device=device))
        for layer in self.hidden:
            relu = layer(relu).clamp(min=0).to(device)
        pred = self.modelOut(relu)
        return pred
    def selectMove(self,input) -> (int,int):
        '''
        input has to be an list with the values
            -1 oponent
            0 empty
            1 self
        returns the X Pos and Y Pos of move
        '''


        output = self(input)
        softmax = nn.Softmax(dim=-1)(output)
        if random() >= self.randomValue:
            print(softmax)
            absInput = torch.abs(torch.tensor(input,dtype=torch.float,device=device))
            ones = torch.ones(len(input),dtype=torch.int8,device=device)
            setValues = torch.logical_and(absInput,ones)
            notSetValues = torch.logical_not(setValues)
            mask2 = notSetValues.long()
            relevantvalues2 = torch.mul(softmax,mask2)
            value = torch.max(relevantvalues2,0)[1].item()
        else:
            value = randrange(9)
            self.randomValue-=self.randomDecay
            while input[value] != 0:
                value = (value+1)%9

        self.memory.append([input,output])
        return (int(value/3),value%3)


    def train(self,reward,printLoss=True):
        for m in self.memory:
            if len(m) == 2:
                m.append(reward)
        total_loss = 0
        for m in self.memory:
            reward = m[2]
            self.optimizer.zero_grad()
            input = m[0]
            output = self(input)
            loss = F.smooth_l1_loss(output,torch.tensor([reward],dtype=torch.float,device=device))
            total_loss +=float(loss)
            loss.backward()
            self.optimizer.step()
        self.memory.clear()
        if printLoss:
            print(total_loss)

def netVersion1():
    #input = torch.from_numpy(np.array([0,0,0,0,0,0,0,0,0],dtype=torch.long))
    input = torch.tensor([1,0,0,0,0,0,0,0,0],dtype=torch.float,device="cuda")
    print(input)
    #quit()
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    n = Network(10,1).to(device)
    output = n(input)
    print("output is {}".format(output))
    m = nn.Softmax(dim=-1)(output)
    print(m)
    a,b = torch.max(m,0)
    print(torch.max(m,0)[1])
    print("Best Value is: {}".format(m.max(0)[1]))
    print(a)
    print(b)
    for x in range(500):
        n.train(0,x%100==0)
    output = n(input)
    print("output is {}".format(output))
    m = nn.Softmax(dim=-1)(output)
    print(m)
    a,b = torch.max(m,0)
    print(torch.max(m,0)[1])
    print("Best Value is: {}".format(m.max(0)[1]))
    print(a)
    print(b)
    print("---------------------------------")
    n.selectMove([-1,0,1,1,0,-1,0,0,0])
    n.selectMove([-1,0,-1,-1,0,-1,0,0,0])
    print(n.memory)

def netVersion2(): 
    input = torch.tensor([1,0,0,0,0,0,0,0,0],dtype=torch.float,device=device)
    for i in range(100):
        n = Network(10,1).to(device)
        print("Selected move is {}".format(n.selectMove(input)))
        n.train(reward=1)
    print("Selected move is {}".format(n.selectMove(input)))
    #output = n(input)
    #print(output)    
    pass   
def netVersion3():
    print("Running on device "+str(device))
    input = torch.tensor([1,0,0,0,-1,0,0,0,0],dtype=torch.float,device=device)
    net = Network(30,1).to(device)
    print(net.selectMove(input))
if __name__ == '__main__':
    netVersion3()