import torch.nn as nn
import torch
import numpy as np
import torch.optim as optim
import torch.nn.functional as F
from random import random,randrange,sample

class Network(nn.Module):
    def __init__(self,H,n):
        '''
        H is number of Hidden neurons
        n is number of hidden layers (minimum 0)
        '''
        n = n if n >=0 else 0
        super(Network,self).__init__()
        self.modelIn = nn.Linear(9,H)
        self.hidden = []
        for i in range(n):
            self.hidden.append(nn.Linear(H,H).to("cuda"))
        self.modelOut=nn.Linear(H,9)

        self.memory = []
        self.optimizer = optim.RMSprop(self.parameters())
        self.randomValue = 0.75
        self.randomDecay = 0.001
        self.epoch = 10
        self.currentEpoch = 0
        

    
    def forward(self, x):
        '''
        Input of nine values for the current state of the 
        TicTacToe Field
        results in a Tensor 
        '''
        relu = self.modelIn(x).clamp(min=0).to("cuda")
        for layer in self.hidden:
            relu = layer(relu).clamp(min=0).to("cuda")
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
        #calculate the network value
        #print("select move start")
        output = None
        if random() > self.randomValue:
            #print("Moved by intention")
            output = self(torch.tensor(input,dtype=torch.float,device="cuda"))
            softmax = nn.Softmax(dim=-1)(output)
            value = torch.max(softmax,0)[1].item()
        else:
            #print("Moved randomly")
            value = randrange(9)
            self.randomValue-=self.randomDecay
        while input[value] != 0:
            value = (value+1)%9
        if output == None:
            output =torch.zeros(9)
            output[value] = 1
        #print("Value = {} ends in X = {} and y= {}".format(value,int(value/3),value%3))
        #print("select move end")
        self.memory.append([input,output])
        return (int(value/3),value%3)
        pass
    def train(self,reward,printLoss=True):
        #if printLoss:
        #    print(self.modelIn.weight)
        total_loss = 0
        for item in self.memory:
            if(len(item) == 2):
                item.append(reward)
        self.currentEpoch+=1
        if self.currentEpoch<self.epoch:
            return
        self.currentEpoch = 0
        #for move in self.memory:
        for i in range(int(self.memory / 10)):
            move = sample(self.memory)
            reward = move[2]
            self.optimizer.zero_grad()
            input = torch.tensor(move[0],dtype=torch.float,device="cuda")
            output = self(input)
            target = torch.tensor(move[1]*reward,dtype=torch.float,device="cuda")
            loss = F.smooth_l1_loss(output,target)
            total_loss+=float(loss)
            loss.backward()
            self.optimizer.step()
        self.memory.clear()
        if printLoss:
            print("loss={}".format(total_loss))
if __name__ == '__main__':
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