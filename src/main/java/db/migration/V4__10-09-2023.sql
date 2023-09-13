CREATE TABLE public.t_acesso_endpoint(
  nome_endpoint character varying,
  qtd_acesso_endpoint integer);
  
  
INSERT INTO public.t_acesso_endpoint(
            nome_endpoint, qtd_acesso_endpoint)
    VALUES ('END-POINT-NOME-PESSOA-FISICA', 0);


alter table t_acesso_endpoint add constraint nome_endpoint_unique UNIQUE (nome_endpoint);