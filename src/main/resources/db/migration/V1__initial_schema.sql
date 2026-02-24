--
-- PostgreSQL database dump
--

-- Dumped from database version 15.16
-- Dumped by pg_dump version 15.16

-- SET statement_timeout = 0;
-- SET lock_timeout = 0;
-- SET idle_in_transaction_session_timeout = 0;
-- SET client_encoding = 'UTF8';
-- SET standard_conforming_strings = on;
-- SELECT pg_catalog.set_config('search_path', '', false);
-- SET check_function_bodies = false;
-- SET xmloption = content;
-- SET client_min_messages = warning;
-- SET row_security = off;

ALTER TABLE IF EXISTS ONLY public.clients DROP CONSTRAINT IF EXISTS fktiuqdledq2lybrds2k3rfqrv4;
ALTER TABLE IF EXISTS ONLY public.appointment_addons_record DROP CONSTRAINT IF EXISTS fkt3gyvigvhli8782hb8os2b6q;
ALTER TABLE IF EXISTS ONLY public.appointment_addons_record DROP CONSTRAINT IF EXISTS fksi4dirh6fkk6rsbjgb3ai87s;
ALTER TABLE IF EXISTS ONLY public.appointment_notification DROP CONSTRAINT IF EXISTS fkpjsd7jd504tlfyecws2xukplk;
ALTER TABLE IF EXISTS ONLY public.retention_forecast DROP CONSTRAINT IF EXISTS fkny77h2kfej4ccm2cgpkrewpj;
ALTER TABLE IF EXISTS ONLY public.appointment DROP CONSTRAINT IF EXISTS fkni4hs6h0bbqj8cc16hccppuuu;
ALTER TABLE IF EXISTS ONLY public.work_schedule DROP CONSTRAINT IF EXISTS fkmn6k06r0ad4aw8s8a49fbb94y;
ALTER TABLE IF EXISTS ONLY public.retention_forecast DROP CONSTRAINT IF EXISTS fkl3918912lweatg7t7gj6351px;
ALTER TABLE IF EXISTS ONLY public.refresh_token DROP CONSTRAINT IF EXISTS fkjtx87i0jvq2svedphegvdwcuy;
ALTER TABLE IF EXISTS ONLY public.retention_forecast DROP CONSTRAINT IF EXISTS fkjfjcmkypvtmdelwl3ouashrn1;
ALTER TABLE IF EXISTS ONLY public.salon_profile DROP CONSTRAINT IF EXISTS fkionbydwwqvit5rjvkay0vl5ey;
ALTER TABLE IF EXISTS ONLY public.professional DROP CONSTRAINT IF EXISTS fkfif9nre2vib9k48065tw91h9k;
ALTER TABLE IF EXISTS ONLY public.retention_forecast DROP CONSTRAINT IF EXISTS fkdr8j6b4qoe62e8581kcewhr72;
ALTER TABLE IF EXISTS ONLY public.service_professionals DROP CONSTRAINT IF EXISTS fkdl9ahe59gntpe4n1up0eftmf1;
ALTER TABLE IF EXISTS ONLY public.client_audit_metrics DROP CONSTRAINT IF EXISTS fkd3d04xebwb4qlraxnirin5xyg;
ALTER TABLE IF EXISTS ONLY public.schedule_block DROP CONSTRAINT IF EXISTS fkcgjga2auhdd647totw973lxny;
ALTER TABLE IF EXISTS ONLY public.appointment DROP CONSTRAINT IF EXISTS fk7y39ubfrch1jv1csekp9rmup6;
ALTER TABLE IF EXISTS ONLY public.appointment DROP CONSTRAINT IF EXISTS fk7wv46g6c222h1bnk4uk2xjod7;
ALTER TABLE IF EXISTS ONLY public.service_professionals DROP CONSTRAINT IF EXISTS fk2etykylku1kjsb0wakh0kr37b;
ALTER TABLE IF EXISTS ONLY public.work_schedule DROP CONSTRAINT IF EXISTS work_schedule_pkey;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS users_pkey;
ALTER TABLE IF EXISTS ONLY public.refresh_token DROP CONSTRAINT IF EXISTS ukr4k4edos30bx9neoq81mdvwph;
ALTER TABLE IF EXISTS ONLY public.service DROP CONSTRAINT IF EXISTS uknjew1c9fl5n5u2fmteo291087;
ALTER TABLE IF EXISTS ONLY public.salon_profile DROP CONSTRAINT IF EXISTS ukl630h31xosqmuxq9lj1s8aj0x;
ALTER TABLE IF EXISTS ONLY public.client_audit_metrics DROP CONSTRAINT IF EXISTS uki686llavv9390id8k4wa2q56e;
ALTER TABLE IF EXISTS ONLY public.retention_forecast DROP CONSTRAINT IF EXISTS ukhybhwstd67u8dj1ocny1yqpxf;
ALTER TABLE IF EXISTS ONLY public.professional DROP CONSTRAINT IF EXISTS ukh9usoa4j6g3l26wt40emh44q8;
ALTER TABLE IF EXISTS ONLY public.refresh_token DROP CONSTRAINT IF EXISTS ukf95ixxe7pa48ryn1awmh2evt7;
ALTER TABLE IF EXISTS ONLY public.clients DROP CONSTRAINT IF EXISTS ukbt1ji0od8t2mhp0thot6pod8u;
ALTER TABLE IF EXISTS ONLY public.service DROP CONSTRAINT IF EXISTS ukadgojnrwwx9c3y3qa2q08uuqp;
ALTER TABLE IF EXISTS ONLY public.work_schedule DROP CONSTRAINT IF EXISTS uk_professional_day;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS uk6dotkott2kjsp8vw4d0m25fb7;
ALTER TABLE IF EXISTS ONLY public.salon_profile DROP CONSTRAINT IF EXISTS uk2bo3ncdoybyk1m533wjuohy1y;
ALTER TABLE IF EXISTS ONLY public.service_professionals DROP CONSTRAINT IF EXISTS service_professionals_pkey;
ALTER TABLE IF EXISTS ONLY public.service DROP CONSTRAINT IF EXISTS service_pkey;
ALTER TABLE IF EXISTS ONLY public.schedule_block DROP CONSTRAINT IF EXISTS schedule_block_pkey;
ALTER TABLE IF EXISTS ONLY public.salon_profile DROP CONSTRAINT IF EXISTS salon_profile_pkey;
ALTER TABLE IF EXISTS ONLY public.salon_daily_revenue DROP CONSTRAINT IF EXISTS salon_daily_revenue_pkey;
ALTER TABLE IF EXISTS ONLY public.retention_forecast DROP CONSTRAINT IF EXISTS retention_forecast_pkey;
ALTER TABLE IF EXISTS ONLY public.refresh_token DROP CONSTRAINT IF EXISTS refresh_token_pkey;
ALTER TABLE IF EXISTS ONLY public.professional DROP CONSTRAINT IF EXISTS professional_pkey;
ALTER TABLE IF EXISTS ONLY public.clients DROP CONSTRAINT IF EXISTS clients_pkey;
ALTER TABLE IF EXISTS ONLY public.client_audit_metrics DROP CONSTRAINT IF EXISTS client_audit_metrics_pkey;
ALTER TABLE IF EXISTS ONLY public.appointment DROP CONSTRAINT IF EXISTS appointment_pkey;
ALTER TABLE IF EXISTS ONLY public.appointment_notification DROP CONSTRAINT IF EXISTS appointment_notification_pkey;
ALTER TABLE IF EXISTS ONLY public.appointment_addons_record DROP CONSTRAINT IF EXISTS appointment_addons_record_pkey;
DROP SEQUENCE IF EXISTS public.work_schedule_seq;
DROP TABLE IF EXISTS public.work_schedule;
DROP SEQUENCE IF EXISTS public.users_seq;
DROP TABLE IF EXISTS public.users;
DROP SEQUENCE IF EXISTS public.service_seq;
DROP TABLE IF EXISTS public.service_professionals;
DROP TABLE IF EXISTS public.service;
DROP SEQUENCE IF EXISTS public.schedule_block_seq;
DROP TABLE IF EXISTS public.schedule_block;
DROP SEQUENCE IF EXISTS public.salon_profile_seq;
DROP TABLE IF EXISTS public.salon_profile;
DROP SEQUENCE IF EXISTS public.salon_daily_revenue_seq;
DROP TABLE IF EXISTS public.salon_daily_revenue;
DROP SEQUENCE IF EXISTS public.retention_forecast_seq;
DROP TABLE IF EXISTS public.retention_forecast;
DROP SEQUENCE IF EXISTS public.refresh_token_seq;
DROP TABLE IF EXISTS public.refresh_token;
DROP TABLE IF EXISTS public.professional;
DROP TABLE IF EXISTS public.clients;
DROP SEQUENCE IF EXISTS public.client_audit_metrics_seq;
DROP TABLE IF EXISTS public.client_audit_metrics;
DROP SEQUENCE IF EXISTS public.appointment_seq;
DROP SEQUENCE IF EXISTS public.appointment_notification_seq;
DROP TABLE IF EXISTS public.appointment_notification;
DROP SEQUENCE IF EXISTS public.appointment_addons_record_seq;
DROP TABLE IF EXISTS public.appointment_addons_record;
DROP TABLE IF EXISTS public.appointment;
SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: appointment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.appointment (
    external_id bigint NOT NULL,
    tenant_id character varying(255) NOT NULL,
    appointment_status character varying(255),
    end_date timestamp(6) with time zone NOT NULL,
    observations character varying(120),
    salon_trade_name character varying(255),
    salon_zone_id character varying(255),
    start_date timestamp(6) with time zone NOT NULL,
    total_value numeric(38,2) NOT NULL,
    client_id bigint NOT NULL,
    main_service_id bigint NOT NULL,
    professional_id bigint NOT NULL,
    CONSTRAINT appointment_appointment_status_check CHECK (((appointment_status)::text = ANY (ARRAY[('PENDING'::character varying)::text, ('CONFIRMED'::character varying)::text, ('CANCELLED'::character varying)::text, ('MISSED'::character varying)::text, ('COMPLETED'::character varying)::text])))
);


--
-- Name: appointment_addons_record; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.appointment_addons_record (
    id bigint NOT NULL,
    tenant_id character varying(255) NOT NULL,
    quantity integer,
    unit_price_at_moment integer,
    service_id bigint,
    appointment_addon_id bigint
);


--
-- Name: appointment_addons_record_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.appointment_addons_record_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: appointment_notification; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.appointment_notification (
    id bigint NOT NULL,
    appointment_notification_status character varying(255) NOT NULL,
    appointment_notification_type character varying(255) NOT NULL,
    destination_number character varying(255),
    external_message_id character varying(255),
    failed_message_content character varying(500),
    sent_at timestamp(6) with time zone,
    appointment_external_id bigint,
    CONSTRAINT appointment_notification_appointment_notification_status_check CHECK (((appointment_notification_status)::text = ANY (ARRAY[('PENDING'::character varying)::text, ('SENT'::character varying)::text, ('DELIVERED'::character varying)::text, ('FAILED'::character varying)::text]))),
    CONSTRAINT appointment_notification_appointment_notification_type_check CHECK (((appointment_notification_type)::text = ANY (ARRAY[('CONFIRMATION'::character varying)::text, ('REMINDER'::character varying)::text])))
);


--
-- Name: appointment_notification_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.appointment_notification_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: appointment_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.appointment_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: client_audit_metrics; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.client_audit_metrics (
    id bigint NOT NULL,
    tenant_id character varying(255) NOT NULL,
    canceled_appointments_count bigint,
    completed_appointments_count bigint,
    last_visit_date timestamp(6) with time zone,
    missed_appointments_count bigint,
    total_spent numeric(19,2),
    client_id bigint NOT NULL
);


--
-- Name: client_audit_metrics_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.client_audit_metrics_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: clients; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.clients (
    cancelled_appointments integer NOT NULL,
    missed_appointments integer NOT NULL,
    phone_number character varying(13) NOT NULL,
    user_id bigint NOT NULL
);


--
-- Name: professional; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.professional (
    external_id uuid NOT NULL,
    is_active boolean NOT NULL,
    is_first_login boolean NOT NULL,
    professional_picture character varying(255),
    user_id bigint NOT NULL
);


--
-- Name: refresh_token; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.refresh_token (
    id bigint NOT NULL,
    expiry_date timestamp(6) with time zone NOT NULL,
    is_revoked boolean,
    token character varying(255) NOT NULL,
    user_id bigint
);


--
-- Name: refresh_token_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.refresh_token_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: retention_forecast; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.retention_forecast (
    id bigint NOT NULL,
    tenant_id character varying(255) NOT NULL,
    predicted_return_date timestamp(6) with time zone NOT NULL,
    status character varying(255) NOT NULL,
    client_id bigint NOT NULL,
    last_service_id bigint NOT NULL,
    origin_appointment_id bigint,
    professional_id bigint,
    CONSTRAINT retention_forecast_status_check CHECK (((status)::text = ANY (ARRAY[('PENDING'::character varying)::text, ('CONVERTED'::character varying)::text, ('EXPIRED'::character varying)::text, ('FAILED_TO_SEND'::character varying)::text])))
);


--
-- Name: retention_forecast_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.retention_forecast_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: salon_daily_revenue; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.salon_daily_revenue (
    id bigint NOT NULL,
    tenant_id character varying(255) NOT NULL,
    appointments_count bigint,
    date date,
    total_revenue numeric(19,2)
);


--
-- Name: salon_daily_revenue_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.salon_daily_revenue_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: salon_profile; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.salon_profile (
    id bigint NOT NULL,
    tenant_id character varying(255) NOT NULL,
    appointment_buffer_minutes integer NOT NULL,
    comercial_phone character varying(11) NOT NULL,
    domain_slug character varying(40) NOT NULL,
    evolution_connection_state character varying(255) NOT NULL,
    full_address character varying(80) NOT NULL,
    is_loyal_clientele_prioritized boolean NOT NULL,
    last_pairing_code character varying(255),
    logo_path character varying(255) NOT NULL,
    loyal_client_booking_window_days integer,
    operational_status character varying(255) NOT NULL,
    primary_color character varying(15) NOT NULL,
    slogan character varying(120),
    social_media_link character varying(50),
    standard_booking_window integer,
    tenant_status character varying(255),
    trade_name character varying(60) NOT NULL,
    warning_message character varying(200),
    whatsapp_last_reset_at timestamp(6) without time zone,
    salon_zone_id character varying(255) NOT NULL,
    owner_id bigint NOT NULL,
    CONSTRAINT salon_profile_evolution_connection_state_check CHECK (((evolution_connection_state)::text = ANY (ARRAY[('CONNECTING'::character varying)::text, ('OPEN'::character varying)::text, ('CLOSE'::character varying)::text]))),
    CONSTRAINT salon_profile_operational_status_check CHECK (((operational_status)::text = ANY (ARRAY[('OPEN'::character varying)::text, ('CLOSED_TEMPORARY'::character varying)::text, ('UNDER_MAINTENANCE'::character varying)::text]))),
    CONSTRAINT salon_profile_tenant_status_check CHECK (((tenant_status)::text = ANY (ARRAY[('ACTIVE'::character varying)::text, ('SUSPENDED'::character varying)::text])))
);


--
-- Name: salon_profile_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.salon_profile_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: schedule_block; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.schedule_block (
    external_id bigint NOT NULL,
    tenant_id character varying(255) NOT NULL,
    date_and_end_time timestamp(6) with time zone NOT NULL,
    date_and_start_time timestamp(6) with time zone NOT NULL,
    is_whole_day_blocked boolean,
    reason character varying(300) NOT NULL,
    professional_id bigint NOT NULL
);


--
-- Name: schedule_block_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.schedule_block_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: service; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.service (
    id bigint NOT NULL,
    tenant_id character varying(255) NOT NULL,
    active boolean NOT NULL,
    description character varying(250) NOT NULL,
    duration_in_seconds integer NOT NULL,
    is_add_on boolean,
    is_deleted boolean NOT NULL,
    maintenance_interval_days integer,
    nail_count integer NOT NULL,
    name character varying(200) NOT NULL,
    requires_loyalty boolean,
    value integer NOT NULL
);


--
-- Name: service_professionals; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.service_professionals (
    salon_service_id bigint NOT NULL,
    professionals_id bigint NOT NULL
);


--
-- Name: service_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.service_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    tenant_id character varying(255) NOT NULL,
    email character varying(255),
    full_name character varying(255),
    password character varying(255),
    status character varying(255),
    user_role character varying(255) NOT NULL,
    CONSTRAINT users_status_check CHECK (((status)::text = ANY (ARRAY[('ACTIVE'::character varying)::text, ('BANNED'::character varying)::text]))),
    CONSTRAINT users_user_role_check CHECK (((user_role)::text = ANY (ARRAY[('ADMIN'::character varying)::text, ('PROFESSIONAL'::character varying)::text, ('CLIENT'::character varying)::text])))
);


--
-- Name: users_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.users_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: work_schedule; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.work_schedule (
    external_id bigint NOT NULL,
    tenant_id character varying(255) NOT NULL,
    day_of_week character varying(255) NOT NULL,
    is_active boolean NOT NULL,
    lunch_break_end_time time(6) without time zone NOT NULL,
    lunch_break_start_time time(6) without time zone NOT NULL,
    end_time time(6) without time zone NOT NULL,
    start_time time(6) without time zone NOT NULL,
    professional_id bigint NOT NULL,
    CONSTRAINT work_schedule_day_of_week_check CHECK (((day_of_week)::text = ANY (ARRAY[('MONDAY'::character varying)::text, ('TUESDAY'::character varying)::text, ('WEDNESDAY'::character varying)::text, ('THURSDAY'::character varying)::text, ('FRIDAY'::character varying)::text, ('SATURDAY'::character varying)::text, ('SUNDAY'::character varying)::text])))
);


--
-- Name: work_schedule_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.work_schedule_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: appointment_addons_record_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.appointment_addons_record_seq', 1, false);


--
-- Name: appointment_notification_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.appointment_notification_seq', 1, false);


--
-- Name: appointment_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.appointment_seq', 1, false);


--
-- Name: client_audit_metrics_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.client_audit_metrics_seq', 1, false);


--
-- Name: refresh_token_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.refresh_token_seq', 1, false);


--
-- Name: retention_forecast_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.retention_forecast_seq', 1, false);


--
-- Name: salon_daily_revenue_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.salon_daily_revenue_seq', 1, false);


--
-- Name: salon_profile_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.salon_profile_seq', 1, false);


--
-- Name: schedule_block_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.schedule_block_seq', 1, false);


--
-- Name: service_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.service_seq', 1, false);


--
-- Name: users_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.users_seq', 1, true);


--
-- Name: work_schedule_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.work_schedule_seq', 1, false);


--
-- Name: appointment_addons_record appointment_addons_record_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment_addons_record
    ADD CONSTRAINT appointment_addons_record_pkey PRIMARY KEY (id);


--
-- Name: appointment_notification appointment_notification_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment_notification
    ADD CONSTRAINT appointment_notification_pkey PRIMARY KEY (id);


--
-- Name: appointment appointment_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT appointment_pkey PRIMARY KEY (external_id);


--
-- Name: client_audit_metrics client_audit_metrics_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_audit_metrics
    ADD CONSTRAINT client_audit_metrics_pkey PRIMARY KEY (id);


--
-- Name: clients clients_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.clients
    ADD CONSTRAINT clients_pkey PRIMARY KEY (user_id);
--
-- Name: professional professional_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.professional
    ADD CONSTRAINT professional_pkey PRIMARY KEY (user_id);


--
-- Name: refresh_token refresh_token_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refresh_token
    ADD CONSTRAINT refresh_token_pkey PRIMARY KEY (id);


--
-- Name: retention_forecast retention_forecast_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.retention_forecast
    ADD CONSTRAINT retention_forecast_pkey PRIMARY KEY (id);


--
-- Name: salon_daily_revenue salon_daily_revenue_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.salon_daily_revenue
    ADD CONSTRAINT salon_daily_revenue_pkey PRIMARY KEY (id);


--
-- Name: salon_profile salon_profile_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.salon_profile
    ADD CONSTRAINT salon_profile_pkey PRIMARY KEY (id);


--
-- Name: schedule_block schedule_block_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedule_block
    ADD CONSTRAINT schedule_block_pkey PRIMARY KEY (external_id);


--
-- Name: service service_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.service
    ADD CONSTRAINT service_pkey PRIMARY KEY (id);


--
-- Name: service_professionals service_professionals_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.service_professionals
    ADD CONSTRAINT service_professionals_pkey PRIMARY KEY (salon_service_id, professionals_id);


--
-- Name: salon_profile uk2bo3ncdoybyk1m533wjuohy1y; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.salon_profile
    ADD CONSTRAINT uk2bo3ncdoybyk1m533wjuohy1y UNIQUE (owner_id);


--
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: work_schedule uk_professional_day; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.work_schedule
    ADD CONSTRAINT uk_professional_day UNIQUE (professional_id, day_of_week);


--
-- Name: service ukadgojnrwwx9c3y3qa2q08uuqp; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.service
    ADD CONSTRAINT ukadgojnrwwx9c3y3qa2q08uuqp UNIQUE (name);


--
-- Name: clients ukbt1ji0od8t2mhp0thot6pod8u; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.clients
    ADD CONSTRAINT ukbt1ji0od8t2mhp0thot6pod8u UNIQUE (phone_number);


--
-- Name: refresh_token ukf95ixxe7pa48ryn1awmh2evt7; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refresh_token
    ADD CONSTRAINT ukf95ixxe7pa48ryn1awmh2evt7 UNIQUE (user_id);


--
-- Name: professional ukh9usoa4j6g3l26wt40emh44q8; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.professional
    ADD CONSTRAINT ukh9usoa4j6g3l26wt40emh44q8 UNIQUE (external_id);


--
-- Name: retention_forecast ukhybhwstd67u8dj1ocny1yqpxf; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.retention_forecast
    ADD CONSTRAINT ukhybhwstd67u8dj1ocny1yqpxf UNIQUE (origin_appointment_id);


--
-- Name: client_audit_metrics uki686llavv9390id8k4wa2q56e; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_audit_metrics
    ADD CONSTRAINT uki686llavv9390id8k4wa2q56e UNIQUE (client_id);


--
-- Name: salon_profile ukl630h31xosqmuxq9lj1s8aj0x; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.salon_profile
    ADD CONSTRAINT ukl630h31xosqmuxq9lj1s8aj0x UNIQUE (domain_slug);


--
-- Name: service uknjew1c9fl5n5u2fmteo291087; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.service
    ADD CONSTRAINT uknjew1c9fl5n5u2fmteo291087 UNIQUE (description);


--
-- Name: refresh_token ukr4k4edos30bx9neoq81mdvwph; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refresh_token
    ADD CONSTRAINT ukr4k4edos30bx9neoq81mdvwph UNIQUE (token);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: work_schedule work_schedule_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.work_schedule
    ADD CONSTRAINT work_schedule_pkey PRIMARY KEY (external_id);

--
-- Name: service_professionals fk2etykylku1kjsb0wakh0kr37b; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.service_professionals
    ADD CONSTRAINT fk2etykylku1kjsb0wakh0kr37b FOREIGN KEY (professionals_id) REFERENCES public.professional(user_id);


--
-- Name: appointment fk7wv46g6c222h1bnk4uk2xjod7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fk7wv46g6c222h1bnk4uk2xjod7 FOREIGN KEY (main_service_id) REFERENCES public.service(id);


--
-- Name: appointment fk7y39ubfrch1jv1csekp9rmup6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fk7y39ubfrch1jv1csekp9rmup6 FOREIGN KEY (professional_id) REFERENCES public.professional(user_id);


--
-- Name: schedule_block fkcgjga2auhdd647totw973lxny; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedule_block
    ADD CONSTRAINT fkcgjga2auhdd647totw973lxny FOREIGN KEY (professional_id) REFERENCES public.professional(user_id);


--
-- Name: client_audit_metrics fkd3d04xebwb4qlraxnirin5xyg; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.client_audit_metrics
    ADD CONSTRAINT fkd3d04xebwb4qlraxnirin5xyg FOREIGN KEY (client_id) REFERENCES public.clients(user_id);


--
-- Name: service_professionals fkdl9ahe59gntpe4n1up0eftmf1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.service_professionals
    ADD CONSTRAINT fkdl9ahe59gntpe4n1up0eftmf1 FOREIGN KEY (salon_service_id) REFERENCES public.service(id);


--
-- Name: retention_forecast fkdr8j6b4qoe62e8581kcewhr72; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.retention_forecast
    ADD CONSTRAINT fkdr8j6b4qoe62e8581kcewhr72 FOREIGN KEY (professional_id) REFERENCES public.professional(user_id);


--
-- Name: professional fkfif9nre2vib9k48065tw91h9k; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.professional
    ADD CONSTRAINT fkfif9nre2vib9k48065tw91h9k FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: salon_profile fkionbydwwqvit5rjvkay0vl5ey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.salon_profile
    ADD CONSTRAINT fkionbydwwqvit5rjvkay0vl5ey FOREIGN KEY (owner_id) REFERENCES public.professional(user_id);


--
-- Name: retention_forecast fkjfjcmkypvtmdelwl3ouashrn1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.retention_forecast
    ADD CONSTRAINT fkjfjcmkypvtmdelwl3ouashrn1 FOREIGN KEY (client_id) REFERENCES public.clients(user_id);


--
-- Name: refresh_token fkjtx87i0jvq2svedphegvdwcuy; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refresh_token
    ADD CONSTRAINT fkjtx87i0jvq2svedphegvdwcuy FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: retention_forecast fkl3918912lweatg7t7gj6351px; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.retention_forecast
    ADD CONSTRAINT fkl3918912lweatg7t7gj6351px FOREIGN KEY (origin_appointment_id) REFERENCES public.appointment(external_id);


--
-- Name: work_schedule fkmn6k06r0ad4aw8s8a49fbb94y; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.work_schedule
    ADD CONSTRAINT fkmn6k06r0ad4aw8s8a49fbb94y FOREIGN KEY (professional_id) REFERENCES public.professional(user_id);


--
-- Name: appointment fkni4hs6h0bbqj8cc16hccppuuu; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fkni4hs6h0bbqj8cc16hccppuuu FOREIGN KEY (client_id) REFERENCES public.clients(user_id);


--
-- Name: retention_forecast fkny77h2kfej4ccm2cgpkrewpj; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.retention_forecast
    ADD CONSTRAINT fkny77h2kfej4ccm2cgpkrewpj FOREIGN KEY (last_service_id) REFERENCES public.service(id);


--
-- Name: appointment_notification fkpjsd7jd504tlfyecws2xukplk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment_notification
    ADD CONSTRAINT fkpjsd7jd504tlfyecws2xukplk FOREIGN KEY (appointment_external_id) REFERENCES public.appointment(external_id);


--
-- Name: appointment_addons_record fksi4dirh6fkk6rsbjgb3ai87s; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment_addons_record
    ADD CONSTRAINT fksi4dirh6fkk6rsbjgb3ai87s FOREIGN KEY (appointment_addon_id) REFERENCES public.appointment(external_id);


--
-- Name: appointment_addons_record fkt3gyvigvhli8782hb8os2b6q; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment_addons_record
    ADD CONSTRAINT fkt3gyvigvhli8782hb8os2b6q FOREIGN KEY (service_id) REFERENCES public.service(id);


--
-- Name: clients fktiuqdledq2lybrds2k3rfqrv4; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.clients
    ADD CONSTRAINT fktiuqdledq2lybrds2k3rfqrv4 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- PostgreSQL database dump complete
--