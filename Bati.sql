--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4
-- Dumped by pg_dump version 16.4

-- Started on 2024-09-24 21:34:54

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 216 (class 1259 OID 18723)
-- Name: client; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client (
    id integer NOT NULL,
    nom character varying(255) NOT NULL,
    adresse character varying(255),
    telephone character varying(20),
    estprofessionnel boolean
);


ALTER TABLE public.client OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 18722)
-- Name: client_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.client_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.client_id_seq OWNER TO postgres;

--
-- TOC entry 4878 (class 0 OID 0)
-- Dependencies: 215
-- Name: client_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.client_id_seq OWNED BY public.client.id;


--
-- TOC entry 224 (class 1259 OID 27034)
-- Name: composant; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.composant (
    id integer NOT NULL,
    nom character varying(255) NOT NULL,
    type character varying(50),
    tauxtva double precision,
    projet_id integer
);


ALTER TABLE public.composant OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 27033)
-- Name: composant_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.composant_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.composant_id_seq OWNER TO postgres;

--
-- TOC entry 4879 (class 0 OID 0)
-- Dependencies: 223
-- Name: composant_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.composant_id_seq OWNED BY public.composant.id;


--
-- TOC entry 218 (class 1259 OID 18765)
-- Name: devis; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.devis (
    id integer NOT NULL,
    montantestime double precision,
    dateemission date,
    datevalidite date,
    accepte boolean,
    projet_id integer
);


ALTER TABLE public.devis OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 18764)
-- Name: devis_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.devis_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.devis_id_seq OWNER TO postgres;

--
-- TOC entry 4880 (class 0 OID 0)
-- Dependencies: 217
-- Name: devis_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.devis_id_seq OWNED BY public.devis.id;


--
-- TOC entry 219 (class 1259 OID 26987)
-- Name: mainoeuvre; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mainoeuvre (
    composant_id integer NOT NULL,
    tauxhoraire double precision,
    heurestravail double precision,
    productiviteouvrier double precision
);


ALTER TABLE public.mainoeuvre OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 26997)
-- Name: materiel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.materiel (
    composant_id integer NOT NULL,
    quantite double precision,
    coutunitaire double precision,
    couttransport double precision,
    coefficientqualite double precision
);


ALTER TABLE public.materiel OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 27008)
-- Name: projet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.projet (
    id integer NOT NULL,
    nomprojet character varying(255) NOT NULL,
    surface double precision,
    margebeneficiaire double precision,
    couttotal double precision,
    etatprojet character varying(50),
    client_id integer,
    CONSTRAINT projet_etatprojet_check CHECK (((etatprojet)::text = ANY ((ARRAY['ENCOURS'::character varying, 'TERMINE'::character varying, 'ANNULE'::character varying])::text[])))
);


ALTER TABLE public.projet OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 27007)
-- Name: projet_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.projet_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.projet_id_seq OWNER TO postgres;

--
-- TOC entry 4881 (class 0 OID 0)
-- Dependencies: 221
-- Name: projet_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.projet_id_seq OWNED BY public.projet.id;


--
-- TOC entry 4711 (class 2604 OID 18726)
-- Name: client id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client ALTER COLUMN id SET DEFAULT nextval('public.client_id_seq'::regclass);


--
-- TOC entry 4714 (class 2604 OID 27037)
-- Name: composant id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.composant ALTER COLUMN id SET DEFAULT nextval('public.composant_id_seq'::regclass);


--
-- TOC entry 4712 (class 2604 OID 18768)
-- Name: devis id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.devis ALTER COLUMN id SET DEFAULT nextval('public.devis_id_seq'::regclass);


--
-- TOC entry 4713 (class 2604 OID 27011)
-- Name: projet id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.projet ALTER COLUMN id SET DEFAULT nextval('public.projet_id_seq'::regclass);


--
-- TOC entry 4717 (class 2606 OID 18730)
-- Name: client client_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (id);


--
-- TOC entry 4727 (class 2606 OID 27039)
-- Name: composant composant_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.composant
    ADD CONSTRAINT composant_pkey PRIMARY KEY (id);


--
-- TOC entry 4719 (class 2606 OID 18770)
-- Name: devis devis_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.devis
    ADD CONSTRAINT devis_pkey PRIMARY KEY (id);


--
-- TOC entry 4721 (class 2606 OID 26991)
-- Name: mainoeuvre mainoeuvre_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mainoeuvre
    ADD CONSTRAINT mainoeuvre_pkey PRIMARY KEY (composant_id);


--
-- TOC entry 4723 (class 2606 OID 27001)
-- Name: materiel materiel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.materiel
    ADD CONSTRAINT materiel_pkey PRIMARY KEY (composant_id);


--
-- TOC entry 4725 (class 2606 OID 27014)
-- Name: projet projet_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.projet
    ADD CONSTRAINT projet_pkey PRIMARY KEY (id);


--
-- TOC entry 4729 (class 2606 OID 27040)
-- Name: composant composant_projet_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.composant
    ADD CONSTRAINT composant_projet_id_fkey FOREIGN KEY (projet_id) REFERENCES public.projet(id);


--
-- TOC entry 4728 (class 2606 OID 27015)
-- Name: projet projet_client_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.projet
    ADD CONSTRAINT projet_client_id_fkey FOREIGN KEY (client_id) REFERENCES public.client(id);


-- Completed on 2024-09-24 21:34:54

--
-- PostgreSQL database dump complete
--

