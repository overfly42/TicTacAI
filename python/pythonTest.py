import pika
print("Test Rabbit MQ")
connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()
#channel.queue_declare(queue='y')
#channel.basic_publish(exchange='',
#                      routing_key='y',
#                      body='Hello World!')
#queue_name = result.method.queue
#channel.queue_bind(exchange='y',queue='y')
result = channel.queue_declare(queue='y', exclusive=False)
queue_name = result.method.queue

channel.queue_bind(exchange='message_from_server', queue=queue_name)

print(' [*] Waiting for logs. To exit press CTRL+C')

def callback(ch, method, properties, body):
    print(" [x] %r" % body)

channel.basic_consume(
    queue=queue_name, on_message_callback=callback, auto_ack=True)

channel.start_consuming()
#connection.close()

