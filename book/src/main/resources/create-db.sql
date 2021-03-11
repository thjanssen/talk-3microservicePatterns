create sequence hibernate_sequence;

CREATE TABLE public.book(id bigint NOT NULL, author character varying(255) COLLATE pg_catalog."default", title character varying(255) COLLATE pg_catalog."default", version integer NOT NULL, CONSTRAINT book_pkey PRIMARY KEY (id))
CREATE TABLE outboxevent(id uuid NOT NULL, aggregatetype character varying COLLATE pg_catalog."default" NOT NULL, aggregateid character varying COLLATE pg_catalog."default" NOT NULL, type character varying COLLATE pg_catalog."default" NOT NULL, payload character varying COLLATE pg_catalog."default" NOT NULL, CONSTRAINT outboxevent_pkey PRIMARY KEY (id));

INSERT INTO public.book(id, author, title, version) VALUES(1, 'Thorben Janssen', 'Hibernate Tips - More than 70 solutions to common Hibernate problems', 0);
INSERT INTO public.book(id, author, title, version) VALUES(2, 'Unknown', 'Out of stock ...', 0);