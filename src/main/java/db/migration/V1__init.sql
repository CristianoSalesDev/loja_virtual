--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.2
-- Dumped by pg_dump version 9.5.2

-- Started on 2023-07-11 21:59:44

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2344 (class 1262 OID 108070)
-- Name: dblojavirtual; Type: DATABASE; Schema: -; Owner: postgres
--

--CREATE DATABASE dblojavirtual WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Portuguese_Brazil.1252' LC_CTYPE = 'Portuguese_Brazil.1252';


ALTER DATABASE dblojavirtual OWNER TO postgres;

--\connect dblojavirtual

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12355)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2347 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 221 (class 1255 OID 116585)
-- Name: validachavepessoa(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION validachavepessoa() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

  declare existe integer;

  begin 
    existe = (select count(1) from t_pessoa_fisica where id = NEW.pessoa_id);
    if(existe <=0 ) then 
     existe = (select count(1) from t_pessoa_juridica where id = NEW.pessoa_id);
    if (existe <= 0) then
      raise exception 'Não foi encontrado o ID ou PK da pessoa para realizar a associação';
     end if;
    end if;
    RETURN NEW;
  end;
  $$;


ALTER FUNCTION public.validachavepessoa() OWNER TO postgres;

--
-- TOC entry 234 (class 1255 OID 116592)
-- Name: validachavepessoafornecedor(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION validachavepessoafornecedor() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

  declare existe integer;

  begin 
    existe = (select count(1) from pessoa_fisica where id = NEW.pessoa_fornecedor_id);
    if(existe <=0 ) then 
     existe = (select count(1) from pessoa_juridica where id = NEW.pessoa_fornecedor_id);
    if (existe <= 0) then
      raise exception 'Não foi encontrado o ID ou PK do fornecedor para realizar a associação';
     end if;
    end if;
    RETURN NEW;
  end;
  $$;


ALTER FUNCTION public.validachavepessoafornecedor() OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 108090)
-- Name: seq_acesso; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_acesso
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_acesso OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 116465)
-- Name: seq_avaliacao_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_avaliacao_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_avaliacao_produto OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 108083)
-- Name: seq_categoria; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_categoria
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_categoria OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 116316)
-- Name: seq_contas_pagar; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_contas_pagar
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_contas_pagar OWNER TO postgres;

--
-- TOC entry 195 (class 1259 OID 116296)
-- Name: seq_contas_receber; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_contas_receber
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_contas_receber OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 116323)
-- Name: seq_cupom; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_cupom
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_cupom OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 108118)
-- Name: seq_endereco; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_endereco
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_endereco OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 116306)
-- Name: seq_fp; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_fp
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_fp OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 116343)
-- Name: seq_img_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_img_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_img_produto OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 116371)
-- Name: seq_item_entrada; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_item_entrada
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_item_entrada OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 116445)
-- Name: seq_item_pedido; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_item_pedido
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_item_pedido OWNER TO postgres;

--
-- TOC entry 182 (class 1259 OID 108076)
-- Name: seq_marca; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_marca
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_marca OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 116406)
-- Name: seq_nfe; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_nfe
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_nfe OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 116359)
-- Name: seq_nota_entrada; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_nota_entrada
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_nota_entrada OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 116408)
-- Name: seq_pedido; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_pedido
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_pedido OWNER TO postgres;

--
-- TOC entry 189 (class 1259 OID 108108)
-- Name: seq_pessoa; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_pessoa
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_pessoa OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 116333)
-- Name: seq_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_produto OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 116391)
-- Name: seq_status_rastreio; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_status_rastreio
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_status_rastreio OWNER TO postgres;

--
-- TOC entry 193 (class 1259 OID 116277)
-- Name: seq_usuario; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_usuario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_usuario OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 185 (class 1259 OID 108085)
-- Name: t_acesso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_acesso (
    id bigint NOT NULL,
    data_cadastro date,
    descricao character varying(255) NOT NULL
);


ALTER TABLE t_acesso OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 116457)
-- Name: t_avaliacao_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_avaliacao_produto (
    id bigint NOT NULL,
    pessoa_id bigint NOT NULL,
    produto_id bigint NOT NULL,
    nota integer NOT NULL,
    descricao text NOT NULL
);


ALTER TABLE t_avaliacao_produto OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 108078)
-- Name: t_categoria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_categoria (
    id bigint NOT NULL,
    data_cadastro date,
    descricao character varying(255) NOT NULL,
    ativo boolean
);


ALTER TABLE t_categoria OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 116308)
-- Name: t_contas_pagar; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_contas_pagar (
    id bigint NOT NULL,
    data_cadastro date,
    data_pagamento date,
    valor_desconto numeric(19,2),
    pessoa_id bigint NOT NULL,
    pessoa_fornecedor_id bigint NOT NULL,
    data_vencimento date NOT NULL,
    descricao text NOT NULL,
    observacao text,
    status_contas_pagar character varying(255) NOT NULL,
    valor_total numeric(19,2) NOT NULL
);


ALTER TABLE t_contas_pagar OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 116289)
-- Name: t_contas_receber; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_contas_receber (
    id bigint NOT NULL,
    data_cadastro date,
    data_pagamento date,
    valor_desconto numeric(19,2),
    pessoa_id bigint NOT NULL,
    data_vencimento date NOT NULL,
    descricao text NOT NULL,
    observacao text,
    status_contas_receber character varying(255) NOT NULL,
    valor_total numeric(19,2) NOT NULL
);


ALTER TABLE t_contas_receber OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 116318)
-- Name: t_cupom_desconto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_cupom_desconto (
    id bigint NOT NULL,
    codigo_cupom character varying(255) NOT NULL,
    data_cadastro date,
    percentual_desconto numeric(19,2),
    valor_real_desconto numeric(19,2),
    ativo boolean,
    data_validade date NOT NULL
);


ALTER TABLE t_cupom_desconto OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 116472)
-- Name: t_endereco; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_endereco (
    id bigint NOT NULL,
    bairro character varying(255) NOT NULL,
    cep character varying(255) NOT NULL,
    cidade character varying(255) NOT NULL,
    complemento character varying(255),
    logradouro character varying(255) NOT NULL,
    numero character varying(255) NOT NULL,
    tipo_endereco character varying(255) NOT NULL,
    uf character varying(255) NOT NULL,
    pessoa_id bigint NOT NULL
);


ALTER TABLE t_endereco OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 116301)
-- Name: t_forma_pagamento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_forma_pagamento (
    id bigint NOT NULL,
    data_cadastro date,
    descricao character varying(255) NOT NULL,
    ativo boolean
);


ALTER TABLE t_forma_pagamento OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 116486)
-- Name: t_imagem_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_imagem_produto (
    id bigint NOT NULL,
    ativo boolean,
    data_cadastro date,
    imagem_miniatura text NOT NULL,
    imagem_orginal text NOT NULL,
    produto_id bigint NOT NULL
);


ALTER TABLE t_imagem_produto OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 116366)
-- Name: t_item_entrada; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_item_entrada (
    id bigint NOT NULL,
    quantidade double precision NOT NULL,
    valor_desconto numeric(19,2),
    valor_unitario numeric(19,2) NOT NULL,
    nota_entrada_id bigint NOT NULL,
    produto_id bigint NOT NULL,
    total_item numeric(19,2) NOT NULL
);


ALTER TABLE t_item_entrada OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 116440)
-- Name: t_item_pedido; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_item_pedido (
    id bigint NOT NULL,
    quantidade double precision NOT NULL,
    pedido_id bigint NOT NULL,
    produto_id bigint NOT NULL
);


ALTER TABLE t_item_pedido OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 108071)
-- Name: t_marca; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_marca (
    id bigint NOT NULL,
    data_cadastro date,
    descricao character varying(255) NOT NULL,
    ativo boolean
);


ALTER TABLE t_marca OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 116499)
-- Name: t_nfe; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_nfe (
    id bigint NOT NULL,
    numero character varying(255) NOT NULL,
    pdf text NOT NULL,
    serie character varying(255) NOT NULL,
    tipo character varying(255) NOT NULL,
    xml text NOT NULL,
    pedido_id bigint NOT NULL
);


ALTER TABLE t_nfe OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 116351)
-- Name: t_nota_entrada; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_nota_entrada (
    id bigint NOT NULL,
    chave character varying(255),
    data_entrada date,
    observacao character varying(255),
    selo character varying(255),
    serie character varying(255),
    valor_desconto numeric(19,2),
    valor_frete numeric(19,2),
    valor_icms numeric(19,2),
    valor_ipi numeric(19,2),
    contas_pagar_id bigint NOT NULL,
    pessoa_id bigint NOT NULL,
    data_emissao date NOT NULL,
    valor_total numeric(19,2) NOT NULL,
    numero_nota character varying(255) NOT NULL
);


ALTER TABLE t_nota_entrada OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 116545)
-- Name: t_pedido; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_pedido (
    id bigint NOT NULL,
    data_cadastro date,
    data_entrega date NOT NULL,
    dias_entrega integer NOT NULL,
    observacao character varying(255),
    valor_desconto numeric(19,2),
    valor_frete numeric(19,2) NOT NULL,
    valor_total numeric(19,2) NOT NULL,
    nfe_id bigint NOT NULL,
    cupom_id bigint,
    endereco_cobranca_id bigint NOT NULL,
    endereco_entrega_id bigint NOT NULL,
    forma_pagamento_id bigint NOT NULL,
    pessoa_id bigint NOT NULL
);


ALTER TABLE t_pedido OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 108092)
-- Name: t_pessoa_fisica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_pessoa_fisica (
    id bigint NOT NULL,
    email character varying(255) NOT NULL,
    nome character varying(255) NOT NULL,
    telefone character varying(255) NOT NULL,
    cpf character varying(255) NOT NULL,
    data_cadastro date,
    data_nascimento date,
    ativo boolean
);


ALTER TABLE t_pessoa_fisica OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 108100)
-- Name: t_pessoa_juridica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_pessoa_juridica (
    id bigint NOT NULL,
    email character varying(255) NOT NULL,
    nome character varying(255) NOT NULL,
    telefone character varying(255) NOT NULL,
    categoria character varying(255),
    cnpj character varying(255) NOT NULL,
    data_cadastro date,
    inscricao_estadual character varying(255),
    inscricao_municipal character varying(255),
    ativo boolean,
    nome_fantasia character varying(255) NOT NULL,
    razao_social character varying(255) NOT NULL
);


ALTER TABLE t_pessoa_juridica OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 116517)
-- Name: t_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_produto (
    id bigint NOT NULL,
    alerta_qtde_estoque boolean,
    qtde_alerta_estoque integer,
    qtde_clique integer,
    qtde_estoque integer NOT NULL,
    altura double precision NOT NULL,
    ativo boolean NOT NULL,
    comprimento double precision NOT NULL,
    data_cadastro date,
    descricao character varying(255) NOT NULL,
    detalhes text,
    largura double precision NOT NULL,
    link character varying(255),
    peso double precision NOT NULL,
    tipo_unidade character varying(255) NOT NULL,
    valor numeric(19,2) NOT NULL
);


ALTER TABLE t_produto OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 116383)
-- Name: t_status_rastreio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_status_rastreio (
    id bigint NOT NULL,
    centro_distribuicao character varying(255),
    cidade character varying(255),
    data_cadastro date,
    estado character varying(255),
    status character varying(255),
    pedido_id bigint NOT NULL
);


ALTER TABLE t_status_rastreio OWNER TO postgres;

--
-- TOC entry 191 (class 1259 OID 116262)
-- Name: t_usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_usuario (
    id bigint NOT NULL,
    data_cadastro date,
    pessoa_id bigint NOT NULL,
    data_atual_senha date NOT NULL,
    login character varying(255) NOT NULL,
    senha character varying(255) NOT NULL
);


ALTER TABLE t_usuario OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 116270)
-- Name: t_usuario_acesso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t_usuario_acesso (
    usuario_id bigint NOT NULL,
    acesso_id bigint NOT NULL
);


ALTER TABLE t_usuario_acesso OWNER TO postgres;

--
-- TOC entry 2348 (class 0 OID 0)
-- Dependencies: 186
-- Name: seq_acesso; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_acesso', 1, false);


--
-- TOC entry 2349 (class 0 OID 0)
-- Dependencies: 215
-- Name: seq_avaliacao_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_avaliacao_produto', 1, false);


--
-- TOC entry 2350 (class 0 OID 0)
-- Dependencies: 184
-- Name: seq_categoria; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_categoria', 1, false);


--
-- TOC entry 2351 (class 0 OID 0)
-- Dependencies: 199
-- Name: seq_contas_pagar; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_contas_pagar', 1, false);


--
-- TOC entry 2352 (class 0 OID 0)
-- Dependencies: 195
-- Name: seq_contas_receber; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_contas_receber', 1, false);


--
-- TOC entry 2353 (class 0 OID 0)
-- Dependencies: 201
-- Name: seq_cupom; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_cupom', 1, false);


--
-- TOC entry 2354 (class 0 OID 0)
-- Dependencies: 190
-- Name: seq_endereco; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_endereco', 1, false);


--
-- TOC entry 2355 (class 0 OID 0)
-- Dependencies: 197
-- Name: seq_fp; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_fp', 1, false);


--
-- TOC entry 2356 (class 0 OID 0)
-- Dependencies: 203
-- Name: seq_img_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_img_produto', 1, false);


--
-- TOC entry 2357 (class 0 OID 0)
-- Dependencies: 207
-- Name: seq_item_entrada; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_item_entrada', 1, false);


--
-- TOC entry 2358 (class 0 OID 0)
-- Dependencies: 213
-- Name: seq_item_pedido; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_item_pedido', 1, false);


--
-- TOC entry 2359 (class 0 OID 0)
-- Dependencies: 182
-- Name: seq_marca; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_marca', 1, false);


--
-- TOC entry 2360 (class 0 OID 0)
-- Dependencies: 210
-- Name: seq_nfe; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_nfe', 1, false);


--
-- TOC entry 2361 (class 0 OID 0)
-- Dependencies: 205
-- Name: seq_nota_entrada; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_nota_entrada', 1, false);


--
-- TOC entry 2362 (class 0 OID 0)
-- Dependencies: 211
-- Name: seq_pedido; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_pedido', 1, false);


--
-- TOC entry 2363 (class 0 OID 0)
-- Dependencies: 189
-- Name: seq_pessoa; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_pessoa', 1, false);


--
-- TOC entry 2364 (class 0 OID 0)
-- Dependencies: 202
-- Name: seq_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_produto', 1, false);


--
-- TOC entry 2365 (class 0 OID 0)
-- Dependencies: 209
-- Name: seq_status_rastreio; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_status_rastreio', 1, false);


--
-- TOC entry 2366 (class 0 OID 0)
-- Dependencies: 193
-- Name: seq_usuario; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_usuario', 1, false);


--
-- TOC entry 2304 (class 0 OID 108085)
-- Dependencies: 185
-- Data for Name: t_acesso; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2333 (class 0 OID 116457)
-- Dependencies: 214
-- Data for Name: t_avaliacao_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO t_avaliacao_produto (id, pessoa_id, produto_id, nota, descricao) VALUES (1, 1, 1, 10, 'TESTE');


--
-- TOC entry 2302 (class 0 OID 108078)
-- Dependencies: 183
-- Data for Name: t_categoria; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2317 (class 0 OID 116308)
-- Dependencies: 198
-- Data for Name: t_contas_pagar; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2313 (class 0 OID 116289)
-- Dependencies: 194
-- Data for Name: t_contas_receber; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2319 (class 0 OID 116318)
-- Dependencies: 200
-- Data for Name: t_cupom_desconto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2335 (class 0 OID 116472)
-- Dependencies: 216
-- Data for Name: t_endereco; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2315 (class 0 OID 116301)
-- Dependencies: 196
-- Data for Name: t_forma_pagamento; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2336 (class 0 OID 116486)
-- Dependencies: 217
-- Data for Name: t_imagem_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2325 (class 0 OID 116366)
-- Dependencies: 206
-- Data for Name: t_item_entrada; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2331 (class 0 OID 116440)
-- Dependencies: 212
-- Data for Name: t_item_pedido; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2300 (class 0 OID 108071)
-- Dependencies: 181
-- Data for Name: t_marca; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2337 (class 0 OID 116499)
-- Dependencies: 218
-- Data for Name: t_nfe; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2323 (class 0 OID 116351)
-- Dependencies: 204
-- Data for Name: t_nota_entrada; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2339 (class 0 OID 116545)
-- Dependencies: 220
-- Data for Name: t_pedido; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2306 (class 0 OID 108092)
-- Dependencies: 187
-- Data for Name: t_pessoa_fisica; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO t_pessoa_fisica (id, email, nome, telefone, cpf, data_cadastro, data_nascimento, ativo) VALUES (1, 'teste@gmail.com', 'teste', '5454545454545', '787878787878787', '2023-07-11', '1973-07-29', true);


--
-- TOC entry 2307 (class 0 OID 108100)
-- Dependencies: 188
-- Data for Name: t_pessoa_juridica; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2338 (class 0 OID 116517)
-- Dependencies: 219
-- Data for Name: t_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO t_produto (id, alerta_qtde_estoque, qtde_alerta_estoque, qtde_clique, qtde_estoque, altura, ativo, comprimento, data_cadastro, descricao, detalhes, largura, link, peso, tipo_unidade, valor) VALUES (1, true, 1, 1, 10, 50, true, 70, '2023-07-11', 'TESTE', 'TESTE', 60, 'http://www.link.com.br', 80.700000000000003, 'UND', 50.00);


--
-- TOC entry 2327 (class 0 OID 116383)
-- Dependencies: 208
-- Data for Name: t_status_rastreio; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2310 (class 0 OID 116262)
-- Dependencies: 191
-- Data for Name: t_usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2311 (class 0 OID 116270)
-- Dependencies: 192
-- Data for Name: t_usuario_acesso; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2116 (class 2606 OID 108089)
-- Name: t_acesso_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_acesso
    ADD CONSTRAINT t_acesso_pkey PRIMARY KEY (id);


--
-- TOC entry 2144 (class 2606 OID 116464)
-- Name: t_avaliacao_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_avaliacao_produto
    ADD CONSTRAINT t_avaliacao_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2114 (class 2606 OID 108082)
-- Name: t_categoria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_categoria
    ADD CONSTRAINT t_categoria_pkey PRIMARY KEY (id);


--
-- TOC entry 2132 (class 2606 OID 116315)
-- Name: t_contas_pagar_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_contas_pagar
    ADD CONSTRAINT t_contas_pagar_pkey PRIMARY KEY (id);


--
-- TOC entry 2128 (class 2606 OID 116293)
-- Name: t_contas_receber_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_contas_receber
    ADD CONSTRAINT t_contas_receber_pkey PRIMARY KEY (id);


--
-- TOC entry 2134 (class 2606 OID 116322)
-- Name: t_cupom_desconto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_cupom_desconto
    ADD CONSTRAINT t_cupom_desconto_pkey PRIMARY KEY (id);


--
-- TOC entry 2146 (class 2606 OID 116479)
-- Name: t_endereco_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_endereco
    ADD CONSTRAINT t_endereco_pkey PRIMARY KEY (id);


--
-- TOC entry 2130 (class 2606 OID 116305)
-- Name: t_forma_pagamento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_forma_pagamento
    ADD CONSTRAINT t_forma_pagamento_pkey PRIMARY KEY (id);


--
-- TOC entry 2148 (class 2606 OID 116493)
-- Name: t_imagem_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_imagem_produto
    ADD CONSTRAINT t_imagem_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2138 (class 2606 OID 116370)
-- Name: t_item_entrada_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_item_entrada
    ADD CONSTRAINT t_item_entrada_pkey PRIMARY KEY (id);


--
-- TOC entry 2142 (class 2606 OID 116444)
-- Name: t_item_pedido_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_item_pedido
    ADD CONSTRAINT t_item_pedido_pkey PRIMARY KEY (id);


--
-- TOC entry 2112 (class 2606 OID 108075)
-- Name: t_marca_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_marca
    ADD CONSTRAINT t_marca_pkey PRIMARY KEY (id);


--
-- TOC entry 2150 (class 2606 OID 116506)
-- Name: t_nfe_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_nfe
    ADD CONSTRAINT t_nfe_pkey PRIMARY KEY (id);


--
-- TOC entry 2136 (class 2606 OID 116358)
-- Name: t_nota_entrada_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_nota_entrada
    ADD CONSTRAINT t_nota_entrada_pkey PRIMARY KEY (id);


--
-- TOC entry 2154 (class 2606 OID 116549)
-- Name: t_pedido_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_pedido
    ADD CONSTRAINT t_pedido_pkey PRIMARY KEY (id);


--
-- TOC entry 2118 (class 2606 OID 108099)
-- Name: t_pessoa_fisica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_pessoa_fisica
    ADD CONSTRAINT t_pessoa_fisica_pkey PRIMARY KEY (id);


--
-- TOC entry 2120 (class 2606 OID 108107)
-- Name: t_pessoa_juridica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_pessoa_juridica
    ADD CONSTRAINT t_pessoa_juridica_pkey PRIMARY KEY (id);


--
-- TOC entry 2152 (class 2606 OID 116524)
-- Name: t_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_produto
    ADD CONSTRAINT t_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2140 (class 2606 OID 116390)
-- Name: t_status_rastreio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_status_rastreio
    ADD CONSTRAINT t_status_rastreio_pkey PRIMARY KEY (id);


--
-- TOC entry 2122 (class 2606 OID 116269)
-- Name: t_usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_usuario
    ADD CONSTRAINT t_usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 2124 (class 2606 OID 116295)
-- Name: uk_daaxevrcvyim4kk8puen8p9a; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_usuario_acesso
    ADD CONSTRAINT uk_daaxevrcvyim4kk8puen8p9a UNIQUE (acesso_id);


--
-- TOC entry 2126 (class 2606 OID 116276)
-- Name: unique_acesso_user; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_usuario_acesso
    ADD CONSTRAINT unique_acesso_user UNIQUE (usuario_id, acesso_id);


--
-- TOC entry 2181 (class 2620 OID 116589)
-- Name: validachavepessoaavaliacaoprodutoinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaavaliacaoprodutoinsert BEFORE INSERT ON t_avaliacao_produto FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2180 (class 2620 OID 116588)
-- Name: validachavepessoaavaliacaoprodutoupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaavaliacaoprodutoupdate BEFORE UPDATE ON t_avaliacao_produto FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2175 (class 2620 OID 116591)
-- Name: validachavepessoacontaspagarinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacontaspagarinsert BEFORE INSERT ON t_contas_pagar FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2174 (class 2620 OID 116590)
-- Name: validachavepessoacontaspagarupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacontaspagarupdate BEFORE UPDATE ON t_contas_pagar FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2173 (class 2620 OID 116597)
-- Name: validachavepessoacontasreceberinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacontasreceberinsert BEFORE INSERT ON t_contas_receber FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2172 (class 2620 OID 116596)
-- Name: validachavepessoacontasreceberupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacontasreceberupdate BEFORE UPDATE ON t_contas_receber FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2183 (class 2620 OID 116599)
-- Name: validachavepessoaenderecoinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaenderecoinsert BEFORE INSERT ON t_endereco FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2182 (class 2620 OID 116598)
-- Name: validachavepessoaenderecoupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaenderecoupdate BEFORE UPDATE ON t_endereco FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2177 (class 2620 OID 116595)
-- Name: validachavepessoafornecedorcontaspagarinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoafornecedorcontaspagarinsert BEFORE INSERT ON t_contas_pagar FOR EACH ROW EXECUTE PROCEDURE validachavepessoafornecedor();


--
-- TOC entry 2176 (class 2620 OID 116594)
-- Name: validachavepessoafornecedorcontaspagarupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoafornecedorcontaspagarupdate BEFORE UPDATE ON t_contas_pagar FOR EACH ROW EXECUTE PROCEDURE validachavepessoafornecedor();


--
-- TOC entry 2179 (class 2620 OID 116601)
-- Name: validachavepessoanotaentradainsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoanotaentradainsert BEFORE INSERT ON t_nota_entrada FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2178 (class 2620 OID 116600)
-- Name: validachavepessoanotaentradaupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoanotaentradaupdate BEFORE UPDATE ON t_nota_entrada FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2185 (class 2620 OID 116603)
-- Name: validachavepessoapedidoinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoapedidoinsert BEFORE INSERT ON t_pedido FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2184 (class 2620 OID 116602)
-- Name: validachavepessoapedidoupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoapedidoupdate BEFORE UPDATE ON t_pedido FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2171 (class 2620 OID 116605)
-- Name: validachavepessoausuarioinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoausuarioinsert BEFORE INSERT ON t_usuario FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2170 (class 2620 OID 116604)
-- Name: validachavepessoausuarioupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoausuarioupdate BEFORE UPDATE ON t_usuario FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2155 (class 2606 OID 116279)
-- Name: acesso_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_usuario_acesso
    ADD CONSTRAINT acesso_fk FOREIGN KEY (acesso_id) REFERENCES t_acesso(id);


--
-- TOC entry 2157 (class 2606 OID 116361)
-- Name: contaspagar_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_nota_entrada
    ADD CONSTRAINT contaspagar_fk FOREIGN KEY (contas_pagar_id) REFERENCES t_contas_pagar(id);


--
-- TOC entry 2167 (class 2606 OID 116565)
-- Name: cupom_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_pedido
    ADD CONSTRAINT cupom_fk FOREIGN KEY (cupom_id) REFERENCES t_cupom_desconto(id);


--
-- TOC entry 2168 (class 2606 OID 116570)
-- Name: endereco_entrega_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_pedido
    ADD CONSTRAINT endereco_entrega_fk FOREIGN KEY (endereco_entrega_id) REFERENCES t_endereco(id);


--
-- TOC entry 2169 (class 2606 OID 116575)
-- Name: forma_pagamento_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_pedido
    ADD CONSTRAINT forma_pagamento_fk FOREIGN KEY (forma_pagamento_id) REFERENCES t_forma_pagamento(id);


--
-- TOC entry 2166 (class 2606 OID 116560)
-- Name: nfe_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_pedido
    ADD CONSTRAINT nfe_fk FOREIGN KEY (nfe_id) REFERENCES t_nfe(id);


--
-- TOC entry 2158 (class 2606 OID 116373)
-- Name: nota_entrada_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_item_entrada
    ADD CONSTRAINT nota_entrada_fk FOREIGN KEY (nota_entrada_id) REFERENCES t_nota_entrada(id);


--
-- TOC entry 2162 (class 2606 OID 116550)
-- Name: pedido_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_item_pedido
    ADD CONSTRAINT pedido_fk FOREIGN KEY (pedido_id) REFERENCES t_pedido(id);


--
-- TOC entry 2165 (class 2606 OID 116555)
-- Name: pedido_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_nfe
    ADD CONSTRAINT pedido_fk FOREIGN KEY (pedido_id) REFERENCES t_pedido(id);


--
-- TOC entry 2160 (class 2606 OID 116580)
-- Name: pedido_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_status_rastreio
    ADD CONSTRAINT pedido_fk FOREIGN KEY (pedido_id) REFERENCES t_pedido(id);


--
-- TOC entry 2163 (class 2606 OID 116525)
-- Name: produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_avaliacao_produto
    ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES t_produto(id);


--
-- TOC entry 2164 (class 2606 OID 116530)
-- Name: produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_imagem_produto
    ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES t_produto(id);


--
-- TOC entry 2159 (class 2606 OID 116535)
-- Name: produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_item_entrada
    ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES t_produto(id);


--
-- TOC entry 2161 (class 2606 OID 116540)
-- Name: produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_item_pedido
    ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES t_produto(id);


--
-- TOC entry 2156 (class 2606 OID 116284)
-- Name: usuario_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_usuario_acesso
    ADD CONSTRAINT usuario_fk FOREIGN KEY (usuario_id) REFERENCES t_usuario(id);


--
-- TOC entry 2346 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2023-07-11 21:59:46

--
-- PostgreSQL database dump complete
--

