CREATE TABLE "offset" (
   topic TEXT NOT NULL,
   partition INTEGER NOT NULL,
   consumer TEXT NOT NULL,
   "offset" BIGINT NOT NULL,
   PRIMARY KEY(topic, partition, consumer)
);


grant all on "offset" to test;
