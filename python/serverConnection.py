import pika
#channel.queue_declare(queue=name)
#channel.basic_publish(exchange='',
#                      routing_key='y',
#                      body='Hello World!')
#queue_name = result.method.queue
#channel.queue_bind(exchange='y',queue='y')

import sys
import random
from functools import reduce
from copy import deepcopy
from matplotlib import pyplot as plt    
from matplotlib import style    
from threading import Thread
from enum import Enum
import NeuralNet as NeuralNet

def log(msg):
    #print(msg)
    pass
class stragegy(Enum):
    random = 1
    rl_map = 2
    rl_nn = 3

class ai:
    def callback(self, ch, method, properties, body):
        msg = str(body)
        log("Message arrived: "+msg)
        delimiter = msg.find(':')
        msg_type = msg[2:delimiter]
        msg_content = dict(x.split("=") for x in msg[delimiter+1:-1].split(":"))
        if hasattr(self,msg_type):
            getattr(self,msg_type)(msg_content)
        else:
            log("No function with name '{}' available".format(msg_type))

    def RegisterOpen(self,content):
        log("RegisterOpen called")
        self.txChannel.basic_publish(exchange="server_in",routing_key=self.name,body="Register:ID="+self.name)

    def StartGame(self, content):
        log("StartGame called")
        self.initField()
        self.history = []
        self.txChannel.basic_publish(exchange="server_in",routing_key=self.name,body="PlayerReady:ID="+self.name)
        log(content)
    
    def Set(self,content):
        log("Set called")
        log(content)
        log(self.field)
        self.field[content['X']][content['Y']]=content['Player']
        log(self.field)
    
    def Turn(self,content):
        #msg = self.Rowvise()
        #msg=self.Random()
        if self.type == stragegy.random:
            msg = self.Random()
        elif self.type == stragegy.rl_map:
            msg = self.Inteligent()
        elif self.type == stragegy.rl_nn:
            msg = self.useNetwork()
        #log("vvvvvvvvvvvvvvvvvvPossible Movesvvvvvvvvvvvvvvvvvvvv")
        #log(self.getPossibleMoves())
        #log("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")
        self.txChannel.basic_publish(exchange="server_in",routing_key=self.name,body=msg)    

    def Rowvise(self)->str:
        for x in self.field.keys():
            for y in self.field[x].keys():
                if self.field[x][y] == "-":
                    msg = "Set:ID={}:X={}:Y={}".format(self.name,x,y)
                    return msg
    def Random(self)->str:
        possibleMoves = self.getPossibleMoves()
        log(possibleMoves)
        xKeys = list(possibleMoves.keys())
        log(xKeys)
        x = random.choice(xKeys)
        y = random.choice(list(possibleMoves[x].keys()))
        return "Set:ID={}:X={}:Y={}".format(self.name,x,y)
    def Inteligent(self)->str:
        moves = self.getPossibleMoves()
        value = float("-inf")
        bestMoveX = random.choice(list(moves.keys()))
        bestMoveY = random.choice(list(moves[bestMoveX].keys()))
        doInteligent = random.uniform(0,1) > self.randomChoise
        log("Initial move is x={} and y={} and will do inteligent? {}".format(bestMoveX,bestMoveY,doInteligent))
        if doInteligent:
            for x in moves.keys():
                for y in moves[x].keys():
                    field = deepcopy(self.field)
                    field[x][y] = self.name
                    hash = self.hash(field)
                    valueTemp = self.states.get(hash) if self.states.get(hash) else 0
                    if valueTemp > value:
                        value = valueTemp
                        bestMoveX=x
                        bestMoveY=y
        log("Result  move is x={} and y={}".format(bestMoveX,bestMoveY))
        #Remember choosen board
        field = deepcopy(self.field)
        field[bestMoveX][bestMoveY] = self.name
        self.history.append(self.hash(field))
        return "Set:ID={}:X={}:Y={}".format(self.name,bestMoveX,bestMoveY)
    def useNetwork(self)->str:
        nnInput= []
        for r in self.field.keys():
            for l in self.field[r].keys():
                if self.field[r][l] == '-':
                    nnInput.append(0)
                elif self.field[r][l] == self.name:
                    nnInput.append(1)
                else:
                    nnInput.append(-1)
        x,y=  self.network.selectMove(nnInput)
        return "Set:ID={}:X={}:Y={}".format(self.name,x,y)
        
    def EndGame(self,content):
        self.round += 1
        self.train(content)
        pass

    def train(self,content):
        log(content)
        log(self.results)
        learningRate = self.learningRate
        rewardValue = 0
        for key in self.results.keys():
            lastResult = self.results[key][-1]
            self.results[key].append(int(content[key]))
            if lastResult != self.results[key][-1]:
                if key == "Tie":
                    rewardValue = self.rewardTie
                elif key == self.comName: 
                    rewardValue = self.rewardWin
                else:
                    rewardValue = self.rewardLose
        if self.type == stragegy.rl_map:
            self.trainMap(rewardValue)
        elif self.type == stragegy.rl_nn:
            self.network.train(reward=rewardValue,printLoss=self.round%100==0)
    def trainMap(self,rewardValue):
        for turn in self.history:
            log("Reward is {} for {}".format(rewardValue,turn))
            if not turn in self.states.keys():
                self.states[turn] = 0
            self.states[turn] += self.learningRate*(self.decay*rewardValue-self.states[turn])    
            rewardValue = self.states[turn]   
        self.randomChoise-=self.randomDecrease     
        pass
    def EndSession(self,content):
        log("Training Done")
        style.use('ggplot')    
        x = [i for i in range(len(self.results["PlayerA"]))]    
           
        plt.ylabel('Result')    
        plt.xlabel('No Of Games')    
        plt.plot(x, self.results["PlayerA"], 'b', label='PlayerA', linewidth=1)  
        plt.plot(x, self.results["PlayerB"], 'r', label='PlayerB', linewidth=1)  
        plt.plot(x, self.results["Tie"], 'g', label='Tie', linewidth=1)    
        plt.legend(["PlayerA","PlayerB","Tie"],loc=4)
        plt.title("AI is {} ({})".format(self.comName,self.name))    
        #fig = plt.figure()    
      
        plt.show()  

        log(self.comName)
        log(self.results)
        self.results = {"PlayerA":[0],"PlayerB":[0],"Tie":[0]}
        log(self.states)
        self.round = 0
        pass
    def RegisterSuccess(self, content):
        log("I am {}.".format(content["Player"]))
        self.comName = content["Player"]
        pass
    def initField(self):
        self.field={
            '0':{'0':'-','1':'-','2':'-'}, 
            '1':{'0':'-','1':'-','2':'-'},
            '2':{'0':'-','1':'-','2':'-'}}
    def getPossibleMoves(self):
        possibleMoves = dict()
        #self.initField()
        #self.field['1']['1'] = "X"
        #log(self.field)
        for k in self.field.keys():
            #log("Key   = {}".format(k))
            #log("Item1 = {}".format(self.field[k]))
            val = {key:val for key, val in self.field[k].items() if val == '-'}
            #log("Value = {}".format(val))
            possibleMoves[k]=val
            #log("Moves = {}".format(possibleMoves))
            if len(possibleMoves[k]) == 0:
                del possibleMoves[k]
        return possibleMoves
    def hash(self,field):
        ll = [list(x.values()) for x in list(field.values())]
        lll = [item for list in ll for item in list]
        s=set(lll)
        if '-' in s:
            s.remove('-')
        if self.name in s:
            s.remove(self.name)
        item = None if len(s) == 0 else  s.pop()
        if item != None:
            lll = [x.replace(item,'X') for x in lll]
        lll = [x.replace(self.name,'O') for x in lll]
        return (str(lll).replace("[","").replace("]","").replace(",","").replace(" ","").replace("'",""))
        
        #elements = set((list(x.values() for x in field.values())))
        #print(elements)
    def __init__(self, name = "AI_01", type=stragegy.rl_map):
        self.name = name
        self.type = type
        self.field={'0':{'0':"-"}, '1':{'0':"-"}}
        connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
        self.rxChannel = connection.channel()
        self.txChannel = connection.channel()
        result = self.rxChannel.queue_declare(queue=self.name, exclusive=False)
        queue_name = result.method.queue

        self.rxChannel.queue_bind(exchange='message_from_server', queue=queue_name,routing_key='all')
        self.rxChannel.queue_bind(exchange='message_from_server', queue=queue_name,routing_key=self.name)
        self.rxChannel.basic_consume(
            queue=queue_name, on_message_callback=self.callback, auto_ack=True)

        self.states = {}
        self.history = []
        self.results = {"PlayerA":[0],"PlayerB":[0],"Tie":[0]}
        self.comName = "N/A"

        self.learningRate = 0.8
        self.rewardTie = 0.2
        self.rewardWin = 1
        self.rewardLose = -1
        self.decay=0.8
        self.randomChoise = 0.2
        self.randomDecrease = 0.001
        self.round = 0

        self.network = NeuralNet.Network(180,3).to("cuda")

        self.rxChannel.start_consuming()
#connection.close()

if __name__ == "__main__":
    log("starting main")
        
    #style.use('ggplot')    
    #x = [10, 12, 13]    
    #y = [8, 16, 6]    
    #x2 = [8, 15, 11]    
    #y2 = [6, 15, 22]    
    #plt.ylabel('Y axis')    
    #plt.xlabel('X axis')    
    #plt.plot(x, y, 'b', label='line one', linewidth=5)    
    #plt.plot(x2, y2, 'r', label='line two', linewidth=5)    
    #plt.title('Epic Info')    
    #fig = plt.figure()    
      
    #plt.show()  
    #AI = ai()
    #names = ["AI_01","AI_02"]
    names = ["AI_01"]
    threads = []
    for n in names:
        threads.append(Thread(target=ai,args=(names[0],stragegy.rl_nn),daemon=True))
        threads[-1].start()
#    threads.append(Thread(target=ai,args=(names[1],stragegy.rl_map),daemon=True))
#    threads[-1].start()
    #n = NeuralNet.Network(20,2)
    #result = n.selectMove([0,0,0,0,0,0,0,0,0])
    #print(result)
    for t in threads:
        t.join()
#    log("Test")
#    AI.initField()
#    AI.field['1']['2']=AI.name
#    AI.field['0']['0']='v'
#    AI.field['0']['2']=AI.name
#    AI.hash(AI.field)
#    AI.Random()
#    log("Done")
    pass