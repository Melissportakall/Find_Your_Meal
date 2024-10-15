-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Anamakine: localhost
-- Üretim Zamanı: 15 Eki 2024, 20:49:27
-- Sunucu sürümü: 10.4.28-MariaDB
-- PHP Sürümü: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `yazlab`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `Malzemeler`
--

CREATE TABLE `Malzemeler` (
  `MalzemeID` int(11) NOT NULL,
  `MalzemeAdi` varchar(100) NOT NULL,
  `ToplamMiktar` varchar(50) NOT NULL,
  `MalzemeBirim` enum('kilo','litre','gram') NOT NULL,
  `BirimFiyat` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `Malzemeler`
--

INSERT INTO `Malzemeler` (`MalzemeID`, `MalzemeAdi`, `ToplamMiktar`, `MalzemeBirim`, `BirimFiyat`) VALUES
(1, 'Patates', '2.0', 'kilo', 5.00),
(2, 'Kaşar Peyniri', '0.5', 'kilo', 25.00),
(3, 'Un', '1.0', 'kilo', 3.00),
(4, 'Domates', '1.0', 'kilo', 4.00),
(5, 'Kıyma', '0.5', 'kilo', 30.00),
(6, 'Bulgur', '0.5', 'kilo', 3.50),
(7, 'Tavuk', '1.0', 'kilo', 20.00),
(8, 'Mercimek', '0.5', 'kilo', 6.00),
(9, 'Yoğurt', '1.0', 'litre', 10.00),
(10, 'Enginar', '1.0', 'kilo', 7.00),
(11, 'Zeytinyağı', '0.5', 'litre', 30.00),
(12, 'Limon', '300.0', 'gram', 1.50),
(13, 'Şeker', '1.0', 'kilo', 4.00),
(14, 'Süt', '1.0', 'litre', 8.00);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `MalzemeTarif`
--

CREATE TABLE `MalzemeTarif` (
  `TarifID` int(11) NOT NULL,
  `MalzemeID` int(11) NOT NULL,
  `MalzemeMiktar` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `MalzemeTarif`
--

INSERT INTO `MalzemeTarif` (`TarifID`, `MalzemeID`, `MalzemeMiktar`) VALUES
(1, 1, 0.5),
(1, 2, 0.2),
(2, 3, 0.5),
(2, 4, 0.5),
(3, 5, 0.3),
(3, 6, 0.4),
(4, 7, 1),
(5, 8, 0.5),
(5, 9, 0.2),
(6, 10, 1),
(7, 11, 0.1),
(8, 12, 300),
(9, 13, 0.5),
(10, 14, 1);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `Tarifler`
--

CREATE TABLE `Tarifler` (
  `TarifID` int(11) NOT NULL,
  `TarifAdi` varchar(100) NOT NULL,
  `Kategori` varchar(50) DEFAULT NULL,
  `HazirlamaSuresi` int(11) NOT NULL,
  `Talimatlar` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `Tarifler`
--

INSERT INTO `Tarifler` (`TarifID`, `TarifAdi`, `Kategori`, `HazirlamaSuresi`, `Talimatlar`) VALUES
(1, 'Kumpir', 'Atıştırmalık', 30, 'Patatesleri haşlayın, soyun ve ezin. Üzerine tereyağı, kaşar peyniri ve malzemeleri ekleyin.'),
(2, 'Pizza', 'Ana Yemek', 45, 'Hamuru yoğurun, açın ve malzemeleri ekleyin. Fırında pişirin.'),
(3, 'Spaghetti Bolognese', 'Ana Yemek', 40, 'Spagettiyi haşlayın, kıymalı sos ile karıştırın.'),
(4, 'Kısır', 'Salata', 20, 'Bulguru sıcak suyla ıslatın, sebzeleri doğrayın ve sosu ekleyin.'),
(5, 'İskender Kebap', 'Ana Yemek', 50, 'Kebap etini pişirin, pide üzerine yerleştirin ve yoğurt ile soslayın.'),
(6, 'Tavuk Tandır', 'Ana Yemek', 60, 'Tavukları marine edin, fırında pişirin ve yanında pilav ile servis edin.'),
(7, 'Mercimek Çorbası', 'Çorba', 30, 'Mercimekleri haşlayın, sebzelerle birlikte blendırdan geçirin.'),
(8, 'Mantı', 'Ana Yemek', 50, 'Hamuru açın, iç harcı ekleyin ve haşlayın. Yoğurt ile servis edin.'),
(9, 'Pancake', 'Kahvaltı', 20, 'Hamuru karıştırın, tavada pişirin ve şurup ile servis edin.'),
(10, 'Zeytinyağlı Enginar', 'Mezeler', 35, 'Enginarları haşlayın, zeytinyağı ve limon ile tatlandırın.');

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `Malzemeler`
--
ALTER TABLE `Malzemeler`
  ADD PRIMARY KEY (`MalzemeID`);

--
-- Tablo için indeksler `MalzemeTarif`
--
ALTER TABLE `MalzemeTarif`
  ADD PRIMARY KEY (`TarifID`,`MalzemeID`),
  ADD KEY `MalzemeID` (`MalzemeID`);

--
-- Tablo için indeksler `Tarifler`
--
ALTER TABLE `Tarifler`
  ADD PRIMARY KEY (`TarifID`);

--
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `Malzemeler`
--
ALTER TABLE `Malzemeler`
  MODIFY `MalzemeID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Tablo için AUTO_INCREMENT değeri `Tarifler`
--
ALTER TABLE `Tarifler`
  MODIFY `TarifID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Dökümü yapılmış tablolar için kısıtlamalar
--

--
-- Tablo kısıtlamaları `MalzemeTarif`
--
ALTER TABLE `MalzemeTarif`
  ADD CONSTRAINT `malzemetarif_ibfk_1` FOREIGN KEY (`TarifID`) REFERENCES `Tarifler` (`TarifID`) ON DELETE CASCADE,
  ADD CONSTRAINT `malzemetarif_ibfk_2` FOREIGN KEY (`MalzemeID`) REFERENCES `Malzemeler` (`MalzemeID`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
