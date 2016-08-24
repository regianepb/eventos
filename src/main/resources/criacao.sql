/*======================================================================================*/
CREATE DATABASE gereventos
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Portuguese_Brazil.1252'
       LC_CTYPE = 'Portuguese_Brazil.1252'
       CONNECTION LIMIT = -1;
/*======================================================================================*/
CREATE TABLE IF NOT EXISTS public.CLASSIF_DESPESAS (
   ID  BIGINT NOT NULL,
   DESCRICAO  VARCHAR(100) NOT NULL,
   CONSTRAINT pk_classif_despesas PRIMARY KEY (ID)
);

INSERT INTO CLASSIF_DESPESAS(ID, DESCRICAO) VALUES(1, 'Alimentação');
INSERT INTO CLASSIF_DESPESAS(ID, DESCRICAO) VALUES(2, 'Bebidas');
INSERT INTO CLASSIF_DESPESAS(ID, DESCRICAO) VALUES(3, 'Decoração');
INSERT INTO CLASSIF_DESPESAS(ID, DESCRICAO) VALUES(4, 'Terceiros');
INSERT INTO CLASSIF_DESPESAS(ID, DESCRICAO) VALUES(5, 'Outros');

/*======================================================================================*/
CREATE TABLE IF NOT EXISTS public.DESPESAS (
   ID  BIGINT NOT NULL,
   DESCRICAO  VARCHAR(100) NOT NULL,
   CLASSIF_DESPESAS_ID BIGINT NOT NULL,
   CONSTRAINT pk_despesas PRIMARY KEY (ID),   
   CONSTRAINT fk_despesas_classif FOREIGN KEY (CLASSIF_DESPESAS_ID) REFERENCES CLASSIF_DESPESAS(ID) ON UPDATE CASCADE ON DELETE RESTRICT
);
INSERT INTO DESPESAS(ID, DESCRICAO, CLASSIF_DESPESAS_ID) VALUES(1, 'Cheff Jeff Buffet', 1);
INSERT INTO DESPESAS(ID, DESCRICAO, CLASSIF_DESPESAS_ID) VALUES(2, 'Pratos', 1);
INSERT INTO DESPESAS(ID, DESCRICAO, CLASSIF_DESPESAS_ID) VALUES(3, 'Chopp lohn', 2);
INSERT INTO DESPESAS(ID, DESCRICAO, CLASSIF_DESPESAS_ID) VALUES(4, 'Refrigerantes', 2);
INSERT INTO DESPESAS(ID, DESCRICAO, CLASSIF_DESPESAS_ID) VALUES(5, 'Toalhas', 3);
INSERT INTO DESPESAS(ID, DESCRICAO, CLASSIF_DESPESAS_ID) VALUES(6, 'Seguranças', 4);

/*======================================================================================*/
CREATE TABLE IF NOT EXISTS public.RECURSOS (
   ID  BIGINT NOT NULL,
   DESCRICAO  VARCHAR(100) NOT NULL,
   CONSTRAINT pk_recursos PRIMARY KEY (ID)
);

INSERT INTO RECURSOS(ID, DESCRICAO) VALUES(1, 'Ingressos');
INSERT INTO RECURSOS(ID, DESCRICAO) VALUES(2, 'Patrocinadores');
INSERT INTO RECURSOS(ID, DESCRICAO) VALUES(3, 'Venda Chopp');
INSERT INTO RECURSOS(ID, DESCRICAO) VALUES(4, 'Venda Refrigerante');
INSERT INTO RECURSOS(ID, DESCRICAO) VALUES(5, 'Venda Água');

/*======================================================================================*/
CREATE TABLE IF NOT EXISTS public.LOCAIS (
   ID  BIGINT NOT NULL,
   DESCRICAO  VARCHAR(100) NOT NULL,
   CONSTRAINT pk_locais PRIMARY KEY (ID)
);

INSERT INTO LOCAIS(ID, DESCRICAO) VALUES(1, 'S. R. Mampituba');
INSERT INTO LOCAIS(ID, DESCRICAO) VALUES(2, 'BBBowling');
INSERT INTO LOCAIS(ID, DESCRICAO) VALUES(3, 'Centro de Eventos Germano Rigo');

/*======================================================================================*/
CREATE TABLE IF NOT EXISTS public.EVENTOS (
   ID BIGINT NOT NULL,
   DESCRICAO  VARCHAR(100) NOT NULL,
   DATA_HORA timestamp NOT NULL,
   QTD_PESSOAS INTEGER NOT NULL,
   LOCAIS_ID BIGINT,      
   CONSTRAINT pk_eventos PRIMARY KEY (ID),
   CONSTRAINT fk_eventos_locais FOREIGN KEY (LOCAIS_ID) REFERENCES LOCAIS(ID) ON UPDATE CASCADE ON DELETE RESTRICT
);

INSERT INTO EVENTOS(ID, DESCRICAO, DATA_HORA, QTD_PESSOAS, LOCAIS_ID) VALUES(1, 'Guerreiros Open Summer', '2016-11-12 20:00', 250, 2);

/*======================================================================================*/
CREATE TABLE IF NOT EXISTS public.EVENTOS_DESPESAS (
   ID BIGINT NOT NULL,
   EVENTOS_ID BIGINT NOT NULL,
   DESPESAS_ID BIGINT NOT NULL,
   QTD NUMERIC(14,2) NOT NULL,
   VALOR NUMERIC(14,2) NOT NULL,
   CONSTRAINT pk_eventos_despesas PRIMARY KEY (ID),
   CONSTRAINT fk_eventos_despesas_eventos FOREIGN KEY (EVENTOS_ID) REFERENCES EVENTOS(ID) ON UPDATE CASCADE ON DELETE RESTRICT,
   CONSTRAINT fk_eventos_despesas FOREIGN KEY (DESPESAS_ID) REFERENCES DESPESAS(ID) ON UPDATE CASCADE ON DELETE RESTRICT
);

INSERT INTO EVENTOS_DESPESAS(ID, EVENTOS_ID, DESPESAS_ID, QTD, VALOR) VALUES(1, 1, 1, 250, 15.00);
INSERT INTO EVENTOS_DESPESAS(ID, EVENTOS_ID, DESPESAS_ID, QTD, VALOR) VALUES(2, 1, 2, 350, 0.45);
INSERT INTO EVENTOS_DESPESAS(ID, EVENTOS_ID, DESPESAS_ID, QTD, VALOR) VALUES(3, 1, 3, 200, 10.00);
INSERT INTO EVENTOS_DESPESAS(ID, EVENTOS_ID, DESPESAS_ID, QTD, VALOR) VALUES(4, 1, 3, 200, 1.50);
INSERT INTO EVENTOS_DESPESAS(ID, EVENTOS_ID, DESPESAS_ID, QTD, VALOR) VALUES(5, 1, 5, 4, 80.00);

/*======================================================================================*/
CREATE TABLE IF NOT EXISTS public.EVENTOS_RECURSOS (
   ID BIGINT NOT NULL,
   EVENTOS_ID BIGINT NOT NULL,
   RECURSOS_ID BIGINT NOT NULL,
   QTD NUMERIC(14,2) NOT NULL,
   VALOR NUMERIC(14,2) NOT NULL,
   CONSTRAINT pk_eventos_recursos PRIMARY KEY (ID),
   CONSTRAINT fk_eventos_recursos_eventos FOREIGN KEY (EVENTOS_ID) REFERENCES EVENTOS(ID) ON UPDATE CASCADE ON DELETE RESTRICT,
   CONSTRAINT fk_eventos_recursos FOREIGN KEY (RECURSOS_ID) REFERENCES RECURSOS(ID) ON UPDATE CASCADE ON DELETE RESTRICT
);

INSERT INTO EVENTOS_RECURSOS(ID, EVENTOS_ID, RECURSOS_ID, QTD, VALOR) VALUES(1, 1, 1, 250, 30.00);
INSERT INTO EVENTOS_RECURSOS(ID, EVENTOS_ID, RECURSOS_ID, QTD, VALOR) VALUES(2, 1, 2, 1, 100.00);
INSERT INTO EVENTOS_RECURSOS(ID, EVENTOS_ID, RECURSOS_ID, QTD, VALOR) VALUES(3, 1, 3, 200, 15.00);
INSERT INTO EVENTOS_RECURSOS(ID, EVENTOS_ID, RECURSOS_ID, QTD, VALOR) VALUES(4, 1, 4, 200, 3.00);
INSERT INTO EVENTOS_RECURSOS(ID, EVENTOS_ID, RECURSOS_ID, QTD, VALOR) VALUES(5, 1, 5, 50, 2.00);

/*======================================================================================*/

CREATE SEQUENCE public.SEQ_CLASSIF_DESPESAS;
CREATE SEQUENCE public.SEQ_DESPESAS;
CREATE SEQUENCE public.SEQ_RECURSOS;
CREATE SEQUENCE public.SEQ_LOCAIS;
CREATE SEQUENCE public.SEQ_EVENTOS;
CREATE SEQUENCE public.SEQ_EVENTOS_DESPESAS;
CREATE SEQUENCE public.SEQ_EVENTOS_RECURSOS;


