CREATE DATABASE GEREVENTS;
USE GEREVENTS;


CREATE TABLE IF NOT EXISTS CLASSIF_DESPESAS (
   ID  BIGINT AUTO_INCREMENT NOT NULL,
   DESCRICAO  VARCHAR(100) NOT NULL
   PRIMARY KEY (ID)
)ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS DESPESAS (
   ID  BIGINT AUTO_INCREMENT NOT NULL,
   DESCRICAO  VARCHAR(100) NOT NULL,
   CLASSIF_DESPESAS_ID BIGINT NOT NULL,
   PRIMARY KEY (ID)
   
   FOREIGN KEY (DESPESAS, CLASSIF_DESPESAS_ID)
	REFERENCES CLASSIF_DESPESAS(ID)
    ON UPDATE CASCADE ON DELETE RESTRICT;
)ENGINE=InnoDB;

	 
CREATE TABLE IF NOT EXISTS RECURSOS (
   ID  BIGINT AUTO_INCREMENT NOT NULL,
   DESCRICAO  VARCHAR(100) NOT NULL,
   PRIMARY KEY (ID)
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS LOCAIS (
   ID  BIGINT AUTO_INCREMENT NOT NULL,
   DESCRICAO  VARCHAR(100) NOT NULL
   PRIMARY KEY (ID)
)ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS EVENTOS (
   ID  BIGINT AUTO_INCREMENT NOT NULL,
   DESCRICAO  VARCHAR(100) NOT NULL,
   DATA_HORA DATE NOT NULL,
   QTD_PESSOAS INTEGER NOT NULL,
   LOCAIS_ID BIGINT,   
   PRIMARY KEY (ID)
   
   
FOREIGN KEY (EVENTOS, LOCAIS_ID)
	REFERENCES LOCAIS(ID)
    ON UPDATE CASCADE ON DELETE RESTRICT;
)ENGINE=InnoDB;



CREATE TABLE IF NOT EXISTS EVENTOS_DESPESAS (
   ID BIGINT AUTO_INCREMENT NOT NULL,
   EVENTOS_ID  BIGINT NOT NULL,
   DESPESAS_ID BIGINT NOT NULL,
   QTD NUMERIC(14,2) NOT NULL,
   VALOR NUMERIC(14,2) NOT NULL,
   PRIMARY KEY (ID)
      
   FOREIGN KEY (EVENTOS_DESPESAS, EVENTOS_ID)
	REFERENCES EVENTOS(ID)
    ON UPDATE CASCADE ON DELETE RESTRICT;
		
   FOREIGN KEY (EVENTOS_DESPESAS, DESPESAS_ID)
	REFERENCES DESPESAS(ID)
    ON UPDATE CASCADE ON DELETE RESTRICT;
)ENGINE=InnoDB;



CREATE TABLE IF NOT EXISTS EVENTOS_RECURSOS (
   ID BIGINT AUTO_INCREMENT NOT NULL,
   EVENTOS_ID  BIGINT NOT NULL,
   RECURSOS_ID BIGINT NOT NULL,
   QTD NUMERIC(14,2) NOT NULL,
   VALOR NUMERIC(14,2) NOT NULL,
   PRIMARY KEY (ID)
      
   FOREIGN KEY (EVENTOS_RECURSOS, EVENTOS_ID)
	REFERENCES EVENTOS(ID)
    ON UPDATE CASCADE ON DELETE RESTRICT;
		
   FOREIGN KEY (EVENTOS_RECURSOS, RECURSOS_ID)
	REFERENCES RECURSOS(ID)
    ON UPDATE CASCADE ON DELETE RESTRICT;
)ENGINE=InnoDB;


/*-------------------------------*/
/*VIEWS*/
/*-------------------------------*/

CREATE VIEW VW_EVENTOS AS
SELECT EVENTOS.DESCRICAO,
       EVENTOS.DATA_HORA,
	   EVENTOS.QTD_PESSOAS,
	   LOCAIS.DESCRICAO
FROM EVENTOS KEY JOIN 
     LOCAIS;


CREATE VIEW VW_DESPESAS AS
SELECT DESPESAS.DESCRICAO AS DESPESAS_DESCRICAO,
       CLASSIF_DESPESAS.DESCRICAO AS CLASSIF_DESP_DESCRICAO,
	   DESPESAS.ID AS DESPESAS_ID
FROM DESPESAS KEY JOIN 
     CLASSIF_DESPESAS;
	 
CREATE VIEW VW_EVENTOS_DESPESAS AS
SELECT DESPESAS_DESCRICAO,       
	   CLASSIF_DESP_DESCRICAO,
	   EVENTOS.ID AS EVENTOS_ID
FROM EVENTOS KEY JOIN
	 EVENTOS_DESPESAS,
     VW_DESPESAS
WHERE EVENTOS_DESPESAS.DESPESAS_ID = VW_DESPESAS.DESPESAS_ID;

	 
	 