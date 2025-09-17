--
-- PostgreSQL database dump
--

\restrict DUuS7WdGTvfucBkMpTIqdBFQXZibGX5PwhvXqv2CzKWybPqDbwyoewWoGNFOeuQ

-- Dumped from database version 15.14
-- Dumped by pg_dump version 15.14

-- Started on 2025-09-17 08:48:28

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
-- TOC entry 217 (class 1259 OID 24742)
-- Name: barang; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.barang (
    kodebarang integer NOT NULL,
    skubarang character varying(50) NOT NULL,
    nama character varying(100) NOT NULL,
    hargabarang numeric(12,2) NOT NULL,
    stok integer DEFAULT 0,
    kategori character varying(100),
    gambar character varying(255)
);


ALTER TABLE public.barang OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 24741)
-- Name: barang_kodebarang_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.barang_kodebarang_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.barang_kodebarang_seq OWNER TO postgres;

--
-- TOC entry 3418 (class 0 OID 0)
-- Dependencies: 216
-- Name: barang_kodebarang_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.barang_kodebarang_seq OWNED BY public.barang.kodebarang;


--
-- TOC entry 229 (class 1259 OID 24801)
-- Name: barangmasuk; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.barangmasuk (
    idbarangmasuk integer NOT NULL,
    kodebarang integer,
    nama character varying(100),
    satuan character varying(50),
    jumlahmasuk integer,
    hargabarang numeric(12,2),
    totalharga numeric(12,2),
    tanggal date,
    supplier character varying(100)
);


ALTER TABLE public.barangmasuk OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 24800)
-- Name: barangmasuk_idbarangmasuk_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.barangmasuk_idbarangmasuk_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.barangmasuk_idbarangmasuk_seq OWNER TO postgres;

--
-- TOC entry 3419 (class 0 OID 0)
-- Dependencies: 228
-- Name: barangmasuk_idbarangmasuk_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.barangmasuk_idbarangmasuk_seq OWNED BY public.barangmasuk.idbarangmasuk;


--
-- TOC entry 225 (class 1259 OID 24780)
-- Name: detailtransaksi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.detailtransaksi (
    iddetailtransaksi integer NOT NULL,
    kodebarang integer,
    idtransaksi integer,
    namabarang character varying(100),
    jumlah integer,
    harga numeric(12,2),
    keterangan text,
    subtotal numeric(12,2)
);


ALTER TABLE public.detailtransaksi OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 24779)
-- Name: detailtransaksi_iddetailtransaksi_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.detailtransaksi_iddetailtransaksi_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.detailtransaksi_iddetailtransaksi_seq OWNER TO postgres;

--
-- TOC entry 3420 (class 0 OID 0)
-- Dependencies: 224
-- Name: detailtransaksi_iddetailtransaksi_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.detailtransaksi_iddetailtransaksi_seq OWNED BY public.detailtransaksi.iddetailtransaksi;


--
-- TOC entry 215 (class 1259 OID 24735)
-- Name: kategori; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kategori (
    idkategori integer NOT NULL,
    namakategori character varying(100) NOT NULL
);


ALTER TABLE public.kategori OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 24734)
-- Name: kategori_idkategori_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.kategori_idkategori_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.kategori_idkategori_seq OWNER TO postgres;

--
-- TOC entry 3421 (class 0 OID 0)
-- Dependencies: 214
-- Name: kategori_idkategori_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.kategori_idkategori_seq OWNED BY public.kategori.idkategori;


--
-- TOC entry 231 (class 1259 OID 24808)
-- Name: keuangan; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.keuangan (
    idkeuangan integer NOT NULL,
    idasal integer,
    jeniskeuangan character varying(50),
    masuk numeric(12,2) DEFAULT 0,
    keluar numeric(12,2) DEFAULT 0,
    tanggal date
);


ALTER TABLE public.keuangan OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 24807)
-- Name: keuangan_idkeuangan_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.keuangan_idkeuangan_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.keuangan_idkeuangan_seq OWNER TO postgres;

--
-- TOC entry 3422 (class 0 OID 0)
-- Dependencies: 230
-- Name: keuangan_idkeuangan_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.keuangan_idkeuangan_seq OWNED BY public.keuangan.idkeuangan;


--
-- TOC entry 221 (class 1259 OID 24762)
-- Name: metodepembayaran; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.metodepembayaran (
    idmetodepembayaran integer NOT NULL,
    namametodepembayaran character varying(50) NOT NULL
);


ALTER TABLE public.metodepembayaran OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 24761)
-- Name: metodepembayaran_idmetodepembayaran_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.metodepembayaran_idmetodepembayaran_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.metodepembayaran_idmetodepembayaran_seq OWNER TO postgres;

--
-- TOC entry 3423 (class 0 OID 0)
-- Dependencies: 220
-- Name: metodepembayaran_idmetodepembayaran_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.metodepembayaran_idmetodepembayaran_seq OWNED BY public.metodepembayaran.idmetodepembayaran;


--
-- TOC entry 219 (class 1259 OID 24752)
-- Name: pengguna; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pengguna (
    idpengguna integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(255) NOT NULL,
    role character varying(20) NOT NULL,
    status character varying(20) DEFAULT 'aktif'::character varying
);


ALTER TABLE public.pengguna OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 24751)
-- Name: pengguna_idpengguna_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pengguna_idpengguna_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pengguna_idpengguna_seq OWNER TO postgres;

--
-- TOC entry 3424 (class 0 OID 0)
-- Dependencies: 218
-- Name: pengguna_idpengguna_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pengguna_idpengguna_seq OWNED BY public.pengguna.idpengguna;


--
-- TOC entry 227 (class 1259 OID 24789)
-- Name: supplier; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.supplier (
    idsupplier integer NOT NULL,
    namasupplier character varying(100) NOT NULL,
    notelp character varying(20),
    status character varying(20) DEFAULT 'aktif'::character varying,
    alamat text
);


ALTER TABLE public.supplier OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 24788)
-- Name: supplier_idsupplier_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.supplier_idsupplier_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.supplier_idsupplier_seq OWNER TO postgres;

--
-- TOC entry 3425 (class 0 OID 0)
-- Dependencies: 226
-- Name: supplier_idsupplier_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.supplier_idsupplier_seq OWNED BY public.supplier.idsupplier;


--
-- TOC entry 223 (class 1259 OID 24769)
-- Name: transaksi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transaksi (
    idtransaksi integer NOT NULL,
    notransaksi character varying(50) NOT NULL,
    namapengguna character varying(50),
    tgl_transaksi timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    subtotal numeric(12,2) DEFAULT 0,
    diskon numeric(12,2) DEFAULT 0,
    grand_total numeric(12,2) DEFAULT 0,
    metodepembayaran character varying(50)
);


ALTER TABLE public.transaksi OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 24768)
-- Name: transaksi_idtransaksi_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.transaksi_idtransaksi_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.transaksi_idtransaksi_seq OWNER TO postgres;

--
-- TOC entry 3426 (class 0 OID 0)
-- Dependencies: 222
-- Name: transaksi_idtransaksi_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.transaksi_idtransaksi_seq OWNED BY public.transaksi.idtransaksi;


--
-- TOC entry 3214 (class 2604 OID 24745)
-- Name: barang kodebarang; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barang ALTER COLUMN kodebarang SET DEFAULT nextval('public.barang_kodebarang_seq'::regclass);


--
-- TOC entry 3227 (class 2604 OID 24804)
-- Name: barangmasuk idbarangmasuk; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barangmasuk ALTER COLUMN idbarangmasuk SET DEFAULT nextval('public.barangmasuk_idbarangmasuk_seq'::regclass);


--
-- TOC entry 3224 (class 2604 OID 24783)
-- Name: detailtransaksi iddetailtransaksi; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detailtransaksi ALTER COLUMN iddetailtransaksi SET DEFAULT nextval('public.detailtransaksi_iddetailtransaksi_seq'::regclass);


--
-- TOC entry 3213 (class 2604 OID 24738)
-- Name: kategori idkategori; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kategori ALTER COLUMN idkategori SET DEFAULT nextval('public.kategori_idkategori_seq'::regclass);


--
-- TOC entry 3228 (class 2604 OID 24811)
-- Name: keuangan idkeuangan; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.keuangan ALTER COLUMN idkeuangan SET DEFAULT nextval('public.keuangan_idkeuangan_seq'::regclass);


--
-- TOC entry 3218 (class 2604 OID 24765)
-- Name: metodepembayaran idmetodepembayaran; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.metodepembayaran ALTER COLUMN idmetodepembayaran SET DEFAULT nextval('public.metodepembayaran_idmetodepembayaran_seq'::regclass);


--
-- TOC entry 3216 (class 2604 OID 24755)
-- Name: pengguna idpengguna; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pengguna ALTER COLUMN idpengguna SET DEFAULT nextval('public.pengguna_idpengguna_seq'::regclass);


--
-- TOC entry 3225 (class 2604 OID 24792)
-- Name: supplier idsupplier; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.supplier ALTER COLUMN idsupplier SET DEFAULT nextval('public.supplier_idsupplier_seq'::regclass);


--
-- TOC entry 3219 (class 2604 OID 24772)
-- Name: transaksi idtransaksi; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaksi ALTER COLUMN idtransaksi SET DEFAULT nextval('public.transaksi_idtransaksi_seq'::regclass);


--
-- TOC entry 3398 (class 0 OID 24742)
-- Dependencies: 217
-- Data for Name: barang; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.barang (kodebarang, skubarang, nama, hargabarang, stok, kategori, gambar) FROM stdin;
1	SKU001	Indomie Goreng	3500.00	100	Makanan	indomie.jpg
2	SKU002	Aqua Botol 600ml	4000.00	200	Minuman	aqua.jpg
3	SKU003	Pulpen Pilot	5000.00	50	ATK	pulpen.jpg
4	SKU004	Kipas Angin Mini	75000.00	20	Elektronik	kipas.jpg
\.


--
-- TOC entry 3410 (class 0 OID 24801)
-- Dependencies: 229
-- Data for Name: barangmasuk; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.barangmasuk (idbarangmasuk, kodebarang, nama, satuan, jumlahmasuk, hargabarang, totalharga, tanggal, supplier) FROM stdin;
1	1	Indomie Goreng	pcs	50	3000.00	150000.00	2025-09-01	PT Sumber Makmur
2	2	Aqua Botol 600ml	botol	100	3500.00	350000.00	2025-09-02	CV Maju Jaya
\.


--
-- TOC entry 3406 (class 0 OID 24780)
-- Dependencies: 225
-- Data for Name: detailtransaksi; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.detailtransaksi (iddetailtransaksi, kodebarang, idtransaksi, namabarang, jumlah, harga, keterangan, subtotal) FROM stdin;
1	1	1	Indomie Goreng	2	3500.00	Pesanan reguler	7000.00
2	2	1	Aqua Botol 600ml	1	3000.00	Air mineral	3000.00
3	3	2	Pulpen Pilot	4	5000.00	ATK kantor	20000.00
\.


--
-- TOC entry 3396 (class 0 OID 24735)
-- Dependencies: 215
-- Data for Name: kategori; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.kategori (idkategori, namakategori) FROM stdin;
1	Makanan
2	Minuman
3	ATK
4	Elektronik
\.


--
-- TOC entry 3412 (class 0 OID 24808)
-- Dependencies: 231
-- Data for Name: keuangan; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.keuangan (idkeuangan, idasal, jeniskeuangan, masuk, keluar, tanggal) FROM stdin;
1	1	Penjualan	10000.00	0.00	2025-09-01
2	2	Pembelian Barang	0.00	350000.00	2025-09-02
\.


--
-- TOC entry 3402 (class 0 OID 24762)
-- Dependencies: 221
-- Data for Name: metodepembayaran; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.metodepembayaran (idmetodepembayaran, namametodepembayaran) FROM stdin;
1	Cash
2	Transfer Bank
3	QRIS
\.


--
-- TOC entry 3400 (class 0 OID 24752)
-- Dependencies: 219
-- Data for Name: pengguna; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pengguna (idpengguna, username, password, role, status) FROM stdin;
1	admin	admin123	admin	aktif
2	kasir1	kasir123	kasir	aktif
3	manager	manager123	manager	aktif
\.


--
-- TOC entry 3408 (class 0 OID 24789)
-- Dependencies: 227
-- Data for Name: supplier; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.supplier (idsupplier, namasupplier, notelp, status, alamat) FROM stdin;
1	PT Sumber Makmur	08123456789	aktif	Jl. Merdeka No.1
2	CV Maju Jaya	082233445566	aktif	Jl. Raya Selatan No.10
\.


--
-- TOC entry 3404 (class 0 OID 24769)
-- Dependencies: 223
-- Data for Name: transaksi; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.transaksi (idtransaksi, notransaksi, namapengguna, tgl_transaksi, subtotal, diskon, grand_total, metodepembayaran) FROM stdin;
1	TRX001	kasir1	2025-09-17 08:48:04.386779	10000.00	0.00	10000.00	Cash
2	TRX002	kasir1	2025-09-17 08:48:04.386779	20000.00	2000.00	18000.00	QRIS
\.


--
-- TOC entry 3427 (class 0 OID 0)
-- Dependencies: 216
-- Name: barang_kodebarang_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.barang_kodebarang_seq', 4, true);


--
-- TOC entry 3428 (class 0 OID 0)
-- Dependencies: 228
-- Name: barangmasuk_idbarangmasuk_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.barangmasuk_idbarangmasuk_seq', 2, true);


--
-- TOC entry 3429 (class 0 OID 0)
-- Dependencies: 224
-- Name: detailtransaksi_iddetailtransaksi_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.detailtransaksi_iddetailtransaksi_seq', 3, true);


--
-- TOC entry 3430 (class 0 OID 0)
-- Dependencies: 214
-- Name: kategori_idkategori_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.kategori_idkategori_seq', 4, true);


--
-- TOC entry 3431 (class 0 OID 0)
-- Dependencies: 230
-- Name: keuangan_idkeuangan_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.keuangan_idkeuangan_seq', 2, true);


--
-- TOC entry 3432 (class 0 OID 0)
-- Dependencies: 220
-- Name: metodepembayaran_idmetodepembayaran_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.metodepembayaran_idmetodepembayaran_seq', 3, true);


--
-- TOC entry 3433 (class 0 OID 0)
-- Dependencies: 218
-- Name: pengguna_idpengguna_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pengguna_idpengguna_seq', 3, true);


--
-- TOC entry 3434 (class 0 OID 0)
-- Dependencies: 226
-- Name: supplier_idsupplier_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.supplier_idsupplier_seq', 2, true);


--
-- TOC entry 3435 (class 0 OID 0)
-- Dependencies: 222
-- Name: transaksi_idtransaksi_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.transaksi_idtransaksi_seq', 2, true);


--
-- TOC entry 3234 (class 2606 OID 24750)
-- Name: barang barang_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barang
    ADD CONSTRAINT barang_pkey PRIMARY KEY (kodebarang);


--
-- TOC entry 3250 (class 2606 OID 24806)
-- Name: barangmasuk barangmasuk_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barangmasuk
    ADD CONSTRAINT barangmasuk_pkey PRIMARY KEY (idbarangmasuk);


--
-- TOC entry 3244 (class 2606 OID 24787)
-- Name: detailtransaksi detailtransaksi_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detailtransaksi
    ADD CONSTRAINT detailtransaksi_pkey PRIMARY KEY (iddetailtransaksi);


--
-- TOC entry 3232 (class 2606 OID 24740)
-- Name: kategori kategori_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kategori
    ADD CONSTRAINT kategori_pkey PRIMARY KEY (idkategori);


--
-- TOC entry 3252 (class 2606 OID 24815)
-- Name: keuangan keuangan_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.keuangan
    ADD CONSTRAINT keuangan_pkey PRIMARY KEY (idkeuangan);


--
-- TOC entry 3240 (class 2606 OID 24767)
-- Name: metodepembayaran metodepembayaran_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.metodepembayaran
    ADD CONSTRAINT metodepembayaran_pkey PRIMARY KEY (idmetodepembayaran);


--
-- TOC entry 3236 (class 2606 OID 24758)
-- Name: pengguna pengguna_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pengguna
    ADD CONSTRAINT pengguna_pkey PRIMARY KEY (idpengguna);


--
-- TOC entry 3238 (class 2606 OID 24760)
-- Name: pengguna pengguna_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pengguna
    ADD CONSTRAINT pengguna_username_key UNIQUE (username);


--
-- TOC entry 3246 (class 2606 OID 24799)
-- Name: supplier supplier_namasupplier_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.supplier
    ADD CONSTRAINT supplier_namasupplier_key UNIQUE (namasupplier);


--
-- TOC entry 3248 (class 2606 OID 24797)
-- Name: supplier supplier_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.supplier
    ADD CONSTRAINT supplier_pkey PRIMARY KEY (idsupplier);


--
-- TOC entry 3242 (class 2606 OID 24778)
-- Name: transaksi transaksi_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaksi
    ADD CONSTRAINT transaksi_pkey PRIMARY KEY (idtransaksi);


-- Completed on 2025-09-17 08:48:28

--
-- PostgreSQL database dump complete
--

\unrestrict DUuS7WdGTvfucBkMpTIqdBFQXZibGX5PwhvXqv2CzKWybPqDbwyoewWoGNFOeuQ

