PGDMP      
                }            bankdb    17.4    17.4     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            �           1262    24576    bankdb    DATABASE     l   CREATE DATABASE bankdb WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en-US';
    DROP DATABASE bankdb;
                     postgres    false            �            1259    24587    bank_account    TABLE     �   CREATE TABLE public.bank_account (
    id integer NOT NULL,
    customer_id integer NOT NULL,
    account_type character varying(50) NOT NULL,
    balance numeric(12,2) DEFAULT 0.00
);
     DROP TABLE public.bank_account;
       public         heap r       postgres    false            �            1259    24586    bank_accounts_id_seq    SEQUENCE     �   CREATE SEQUENCE public.bank_accounts_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.bank_accounts_id_seq;
       public               postgres    false    220            �           0    0    bank_accounts_id_seq    SEQUENCE OWNED BY     L   ALTER SEQUENCE public.bank_accounts_id_seq OWNED BY public.bank_account.id;
          public               postgres    false    219            �            1259    24578    customer    TABLE     �   CREATE TABLE public.customer (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(100) NOT NULL,
    account_number bigint
);
    DROP TABLE public.customer;
       public         heap r       postgres    false            �            1259    24577    customers_id_seq    SEQUENCE     �   CREATE SEQUENCE public.customers_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.customers_id_seq;
       public               postgres    false    218            �           0    0    customers_id_seq    SEQUENCE OWNED BY     D   ALTER SEQUENCE public.customers_id_seq OWNED BY public.customer.id;
          public               postgres    false    217            �            1259    24629    transaction    TABLE     �   CREATE TABLE public.transaction (
    id integer NOT NULL,
    account_id integer NOT NULL,
    details text,
    amount character varying(50) NOT NULL,
    "timestamp" timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);
    DROP TABLE public.transaction;
       public         heap r       postgres    false            �            1259    24628    transactions_id_seq    SEQUENCE     �   CREATE SEQUENCE public.transactions_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.transactions_id_seq;
       public               postgres    false    222            �           0    0    transactions_id_seq    SEQUENCE OWNED BY     J   ALTER SEQUENCE public.transactions_id_seq OWNED BY public.transaction.id;
          public               postgres    false    221            ,           2604    24590    bank_account id    DEFAULT     s   ALTER TABLE ONLY public.bank_account ALTER COLUMN id SET DEFAULT nextval('public.bank_accounts_id_seq'::regclass);
 >   ALTER TABLE public.bank_account ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    220    219    220            +           2604    24581    customer id    DEFAULT     k   ALTER TABLE ONLY public.customer ALTER COLUMN id SET DEFAULT nextval('public.customers_id_seq'::regclass);
 :   ALTER TABLE public.customer ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    218    217    218            .           2604    24632    transaction id    DEFAULT     q   ALTER TABLE ONLY public.transaction ALTER COLUMN id SET DEFAULT nextval('public.transactions_id_seq'::regclass);
 =   ALTER TABLE public.transaction ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    222    221    222            �          0    24587    bank_account 
   TABLE DATA           N   COPY public.bank_account (id, customer_id, account_type, balance) FROM stdin;
    public               postgres    false    220   l!       �          0    24578    customer 
   TABLE DATA           P   COPY public.customer (id, name, username, password, account_number) FROM stdin;
    public               postgres    false    218   �!       �          0    24629    transaction 
   TABLE DATA           S   COPY public.transaction (id, account_id, details, amount, "timestamp") FROM stdin;
    public               postgres    false    222   "       �           0    0    bank_accounts_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.bank_accounts_id_seq', 10, true);
          public               postgres    false    219            �           0    0    customers_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.customers_id_seq', 4, true);
          public               postgres    false    217            �           0    0    transactions_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.transactions_id_seq', 16, true);
          public               postgres    false    221            7           2606    24593    bank_account bank_accounts_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.bank_account
    ADD CONSTRAINT bank_accounts_pkey PRIMARY KEY (id);
 I   ALTER TABLE ONLY public.bank_account DROP CONSTRAINT bank_accounts_pkey;
       public                 postgres    false    220            1           2606    24645 %   customer customers_account_number_key 
   CONSTRAINT     j   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customers_account_number_key UNIQUE (account_number);
 O   ALTER TABLE ONLY public.customer DROP CONSTRAINT customers_account_number_key;
       public                 postgres    false    218            3           2606    24583    customer customers_pkey 
   CONSTRAINT     U   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customers_pkey PRIMARY KEY (id);
 A   ALTER TABLE ONLY public.customer DROP CONSTRAINT customers_pkey;
       public                 postgres    false    218            5           2606    24585    customer customers_username_key 
   CONSTRAINT     ^   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customers_username_key UNIQUE (username);
 I   ALTER TABLE ONLY public.customer DROP CONSTRAINT customers_username_key;
       public                 postgres    false    218            9           2606    24637    transaction transactions_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT transactions_pkey PRIMARY KEY (id);
 G   ALTER TABLE ONLY public.transaction DROP CONSTRAINT transactions_pkey;
       public                 postgres    false    222            :           2606    24594 +   bank_account bank_accounts_customer_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.bank_account
    ADD CONSTRAINT bank_accounts_customer_id_fkey FOREIGN KEY (customer_id) REFERENCES public.customer(id) ON DELETE CASCADE;
 U   ALTER TABLE ONLY public.bank_account DROP CONSTRAINT bank_accounts_customer_id_fkey;
       public               postgres    false    218    4659    220            ;           2606    24638    transaction fk_account    FK CONSTRAINT     �   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES public.bank_account(id) ON DELETE CASCADE;
 @   ALTER TABLE ONLY public.transaction DROP CONSTRAINT fk_account;
       public               postgres    false    220    4663    222            �   6   x���4�N,��K/�46�30� �8g���r��D8M`B`�=... �9#      �   E   x�3������JNC#cN#cCSsssK3.N�Լ��<eh�ild�ibllf`lnljnd����� u�      �   �   x���KN1D��S����n�96��4����l�!|$/�+W��V������T�"F�;\�D҅:JMgD���c���l**�%c��%�C�����ނ�C+�?�j7�(�9�>������c`+RGf7�썸���0oގ��<�*�#D,����-7��̉@4���*Fi@��9����ʢ��ED�-ٳ�(����yd�,Sg�jG�$ ]ϛV%קY\�$U�I������p3��jᴚxC�d��
 �1&��     