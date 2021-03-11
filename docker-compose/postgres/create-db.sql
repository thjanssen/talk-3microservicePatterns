--
-- Create database for order service
--
CREATE DATABASE "order";
\c "order"
CREATE TABLE public.purchase_order(id bigint NOT NULL, amount double precision, customer_name character varying(255) COLLATE pg_catalog."default", saga_operation character varying(255) COLLATE pg_catalog."default", state character varying(255) COLLATE pg_catalog."default", version integer NOT NULL, CONSTRAINT purchase_order_pkey PRIMARY KEY (id));

CREATE TABLE "purchase_order_position"(id bigint NOT NULL, book_id bigint, quantity integer NOT NULL, version integer NOT NULL, "order_id" bigint, CONSTRAINT "purchase_order_position_pkey" PRIMARY KEY (id), CONSTRAINT fkouh5ikkc66g6pwhd1oht6u9vd FOREIGN KEY ("order_id") REFERENCES "purchase_order" (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION);

create sequence hibernate_sequence;

CREATE TABLE outboxevent(id uuid NOT NULL, aggregatetype character varying COLLATE pg_catalog."default" NOT NULL, aggregateid character varying COLLATE pg_catalog."default" NOT NULL, type character varying COLLATE pg_catalog."default" NOT NULL, payload character varying COLLATE pg_catalog."default" NOT NULL, CONSTRAINT outboxevent_pkey PRIMARY KEY (id));

CREATE TABLE public.revinfo(rev integer NOT NULL, revtstmp bigint, CONSTRAINT revinfo_pkey PRIMARY KEY (rev));

CREATE TABLE public.purchase_order_aud(id bigint NOT NULL, rev integer NOT NULL, revtype smallint, amount double precision, customer_name character varying(255) COLLATE pg_catalog."default", saga_operation character varying(255) COLLATE pg_catalog."default", state character varying(255) COLLATE pg_catalog."default", CONSTRAINT purchase_order_aud_pkey PRIMARY KEY (id, rev), CONSTRAINT fko1dvrmvqx63p7njlgx4g1q9q5 FOREIGN KEY (rev) REFERENCES public.revinfo (rev) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION);


--
-- Create database for view database version of order-info service
--
CREATE DATABASE "order-info";
\c "order-info"
CREATE TABLE "order_info"(id bigint NOT NULL, customer_name character varying(255) COLLATE pg_catalog."default", state character varying(255) COLLATE pg_catalog."default", version integer NOT NULL, CONSTRAINT "order_info_pkey" PRIMARY KEY (id));
CREATE TABLE "order_position"(id bigint NOT NULL, available boolean, book_id bigint, book character varying(255) COLLATE pg_catalog."default", quantity integer NOT NULL, version integer NOT NULL, "order_id" bigint, CONSTRAINT "order_position_pkey" PRIMARY KEY (id), CONSTRAINT fkfigv96vqbdfojshx0d29yij01 FOREIGN KEY ("order_id") REFERENCES "order_info" (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION);


--
-- Create database for book service
--
CREATE DATABASE book;
\c "book"
create sequence hibernate_sequence;

CREATE TABLE book(id bigint NOT NULL, author character varying(255) COLLATE pg_catalog."default", title character varying(255) COLLATE pg_catalog."default", version integer NOT NULL, CONSTRAINT book_pkey PRIMARY KEY (id));
CREATE TABLE outboxevent(id uuid NOT NULL, aggregatetype character varying COLLATE pg_catalog."default" NOT NULL, aggregateid character varying COLLATE pg_catalog."default" NOT NULL, type character varying COLLATE pg_catalog."default" NOT NULL, payload character varying COLLATE pg_catalog."default" NOT NULL, CONSTRAINT outboxevent_pkey PRIMARY KEY (id));

INSERT INTO book(id, author, title, version) VALUES(nextval('hibernate_sequence'), 'Thorben Janssen', 'Hibernate Tips - More than 70 solutions to common Hibernate problems', 0);
INSERT INTO book(id, author, title, version) VALUES(nextval('hibernate_sequence'), 'Unknown', 'Out of stock ...', 0);


--
-- Create database for inventory service
--
CREATE DATABASE inventory;
\c "inventory"

create sequence hibernate_sequence;

CREATE TABLE outboxevent(id uuid NOT NULL, aggregatetype character varying COLLATE pg_catalog."default" NOT NULL, aggregateid character varying COLLATE pg_catalog."default" NOT NULL, type character varying COLLATE pg_catalog."default" NOT NULL, payload character varying COLLATE pg_catalog."default", CONSTRAINT outboxevent_pkey PRIMARY KEY (id));

CREATE TABLE inventory(id bigint NOT NULL, book_id bigint, quantity integer NOT NULL, version integer NOT NULL, CONSTRAINT inventory_pkey PRIMARY KEY (id));

CREATE TABLE inventory_reservation(id bigint NOT NULL, order_id bigint, book_id bigint, quantity integer NOT NULL, version integer NOT NULL, CONSTRAINT inventory_reservation_pkey PRIMARY KEY (id));

INSERT INTO inventory (id, book_id, quantity, version) VALUES (nextval('hibernate_sequence'), 1, 100, 0);
INSERT INTO inventory (id, book_id, quantity, version) VALUES (nextval('hibernate_sequence'), 2, 0, 0);


--
-- Create database for review service
--
CREATE DATABASE review;
\c "review"

create sequence hibernate_sequence;

CREATE TABLE public.review(id bigint NOT NULL, book_id bigint, message character varying(255) COLLATE pg_catalog."default", version integer NOT NULL, CONSTRAINT review_pkey PRIMARY KEY (id));

INSERT INTO review(id, book_id, message, version) VALUES (nextval('hibernate_sequence'), 1, 'Great book', 0);


--
-- Create database for invoice service
--
CREATE DATABASE invoice;
\c "invoice"

CREATE TABLE outboxevent(id uuid NOT NULL, aggregatetype character varying COLLATE pg_catalog."default" NOT NULL, aggregateid character varying COLLATE pg_catalog."default" NOT NULL, type character varying COLLATE pg_catalog."default" NOT NULL, payload character varying COLLATE pg_catalog."default", CONSTRAINT outboxevent_pkey PRIMARY KEY (id));

--
-- Create database for payment service
--
CREATE DATABASE payment;
\c "payment"

CREATE TABLE public.purchase_order(id bigint NOT NULL, amount double precision, CONSTRAINT purchase_order_pkey PRIMARY KEY (id));

CREATE TABLE outboxevent(id uuid NOT NULL, aggregatetype character varying COLLATE pg_catalog."default" NOT NULL, aggregateid character varying COLLATE pg_catalog."default" NOT NULL, type character varying COLLATE pg_catalog."default" NOT NULL, payload character varying COLLATE pg_catalog."default", CONSTRAINT outboxevent_pkey PRIMARY KEY (id));
