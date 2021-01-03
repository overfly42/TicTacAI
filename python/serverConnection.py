import pika
#channel.queue_declare(queue=name)
#channel.basic_publish(exchange='',
#                      routing_key='y',
#                      body='Hello World!')
#queue_name = result.method.queue
#channel.queue_bind(exchange='y',queue='y')

import sys
class ai:
    def callback(self, ch, method, properties, body):
        msg = str(body)
        print("Message arrived: "+msg)
        delimiter = msg.find(':')
        msg_type = msg[2:delimiter]
        msg_content = dict(x.split("=") for x in msg[delimiter+1:-1].split(":"))
        if hasattr(self,msg_type):
            getattr(self,msg_type)(msg_content)
        else:
            print("No function with name '{}' available".format(msg_type))

    def RegisterOpen(self,content):
        print("RegisterOpen called")
        self.txChannel.basic_publish(exchange="server_in",routing_key=self.name,body="Register:ID="+self.name)

    def StartGame(self, content):
        print("StartGame called")
        self.initField()
        self.txChannel.basic_publish(exchange="server_in",routing_key=self.name,body="PlayerReady:ID="+self.name)
        print(content)
    
    def Set(self,content):
        print("Set called")
        print(content)
        print(self.field)
        self.field[content['X']][content['Y']]=content['Player']
        print(self.field)
    
    def Turn(self,content):
        for x in self.field.keys():
            for y in self.field[x].keys():
                if self.field[x][y] == "-":
                    msg = "Set:ID={}:X={}:Y={}".format(self.name,x,y)
                    self.txChannel.basic_publish(exchange="server_in",routing_key=self.name,body=msg)    
                    return
    def EndGame(self,content):
        pass
    def EndSession(self,content):
        pass
    def RegisterSuccess(self, content):
        print("I am {}.".format(content["Player"]))
        pass
    def initField(self):
        self.field={
            '0':{'0':"-","1":'-',"2":"-"}, 
            '1':{'0':"-","1":'-',"2":"-"},
            '2':{'0':"-","1":'-',"2":"-"}}
        
    def __init__(self):
        QUEUE_NAME="NN_AI"
        self.name = "AI_01"
        self.field={'0':{'0':"-"}, '1':{'0':"-"}}
        connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
        self.rxChannel = connection.channel()
        self.txChannel = connection.channel()
        result = self.rxChannel.queue_declare(queue=QUEUE_NAME, exclusive=False)
        queue_name = result.method.queue

        self.rxChannel.queue_bind(exchange='message_from_server', queue=queue_name,routing_key='all')
        self.rxChannel.queue_bind(exchange='message_from_server', queue=queue_name,routing_key=self.name)
        self.rxChannel.basic_consume(
            queue=queue_name, on_message_callback=self.callback, auto_ack=True)

        self.rxChannel.start_consuming()
#connection.close()

if __name__ == "__main__":
    print("starting main")
    AI = ai()
    pass