--
-- PostgreSQL database dump
--

-- Dumped from database version 15.0
-- Dumped by pg_dump version 15.0

-- Started on 2023-02-23 21:00:10

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
-- TOC entry 215 (class 1259 OID 16418)
-- Name: tokens; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tokens (
    id character varying(50) NOT NULL,
    email character varying(50) NOT NULL,
    token character varying(600) NOT NULL
);


ALTER TABLE public.tokens OWNER TO postgres;

--
-- TOC entry 3318 (class 0 OID 16418)
-- Dependencies: 215
-- Data for Name: tokens; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3175 (class 2606 OID 16422)
-- Name: tokens tokens_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tokens
    ADD CONSTRAINT tokens_pkey PRIMARY KEY (id);


-- Completed on 2023-02-23 21:00:10

--
-- PostgreSQL database dump complete
--

