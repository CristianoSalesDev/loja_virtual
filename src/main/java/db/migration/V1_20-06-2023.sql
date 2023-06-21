select constraint_name from information_schema.constraint_column_usage 
where table_name = 't_usuario_acesso' and column_name = 'acesso_id'
and constraint_name <> 'unique_acesso_user';

alter table t_usuario_acesso drop CONSTRAINT "uk_daaxevrcvyim4kk8puen8p9a";