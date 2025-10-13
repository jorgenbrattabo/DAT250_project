For this task, I have added code in Pollcontroller and Votecontroller so that I register RabbbitMQ queues for each poll and publish vote events to RabbitMQ. Also, I made Voteeventlistener.Java for listening to poll queues, processing vote events, and update data. I also made Simple

Everything has mostly gone well. Because i forgot to run redis on docker, i spent quite some time finding out why fullscenariotest did not work.

However, everything seems to work now and when i run SimpleProducer.java, I get a "message received" message in the terminal.