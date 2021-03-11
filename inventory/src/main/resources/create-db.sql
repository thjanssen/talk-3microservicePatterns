
create sequence public.hibernate_sequence;

CREATE TABLE public.outboxevent(id uuid NOT NULL, aggregatetype character varying COLLATE pg_catalog."default" NOT NULL, aggregateid character varying COLLATE pg_catalog."default" NOT NULL, type character varying COLLATE pg_catalog."default" NOT NULL, payload character varying COLLATE pg_catalog."default", CONSTRAINT outboxevent_pkey PRIMARY KEY (id));
CREATE TABLE public.inventory(id bigint NOT NULL, book_id bigint, quantity integer NOT NULL, version integer NOT NULL, CONSTRAINT inventory_pkey PRIMARY KEY (id));

INSERT INTO inventory (id, book_id, quantity, version) VALUES (1, 1, 100, 0);
INSERT INTO inventory (id, book_id, quantity, version) VALUES (2, 2, 0, 0);