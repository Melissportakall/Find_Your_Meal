KOCAELİ ÜNİVERSİTESİ BİLGİSAYAR MÜHENDİSLİĞİ BÖLÜMÜ
YAZILIM LAB. I - I. Proje PROJE TESLİM TARİHİ: 24.10.2024
Tarif Rehberi Uygulaması



Kullanıcının yemek tariflerini saklayabileceği ve eldeki mevcut malzemelerle hangi yemeklerin yapılabileceğini gösteren bir masaüstü uygulaması geliştirilecektir. Uygulama, veritabanı yönetimi, dinamik arama ve filtreleme gibi işlevleri desteklemelidir.


Amaç:
1. Dinamik arama ve filtreleme özelliklerine sahip bir masaüstü uygulaması geliştirilmesi.
2. Bir uygulama içerisinde istenilen özellikteki ürünlerin filtrelenmesi ve sıralanması özelliklerini sağlamak.
3. Veritabanı yönetimi ve algoritma geliştirme konularındaki becerilerin geliştirilmesi.
4. Kullanıcı arayüzü tasarımı ve kullanıcı dostu yazılım geliştirme hakkında deneyim
kazandırılması.
Programlama Dili: C# ya da JAVA.

1. Veritabanı Tasarımı
Veritabanı, tarifleri ve malzemeleri yönetmek için birkaç ilişkilendirilmiş tabloya sahip olacaktır. Veritabanı Tabloları

● Tarifler Tablosu:
○ TarifID (Primary Key, int): Her tarif için benzersiz bir ID.
○ TarifAdi (varchar): Tarifin adı.
○ Kategori (varchar): Tarifin hangi kategoriye ait olduğu (Her tarif yalnız bir
kategoriyle eşleştirilmelidir) (Ana Yemek, Tatlı, vb.)
○ HazirlamaSuresi (int): Tarifin hazırlanma süresi (dakika cinsinden).
○ Talimatlar (text): Tarifin hazırlanış adımları.

● Malzemeler Tablosu:
○ MalzemeID (Primary Key, int): Her malzeme için benzersiz bir ID.
○ MalzemeAdi (varchar): Malzemenin adı.
○ ToplamMiktar (varchar): Malzemenin depodaki toplam miktarı
○ MalzemeBirim: (örneğin "kilo", “litre”, “gram” ).
○ BirimFiyat: Malzemenin birim miktarının maliyet değeri.

● Tarif-Malzeme İlişkisi Tablosu:
○ TarifID (Foreign Key): İlgili tarifin ID'si.
○ MalzemeID (Foreign Key): İlgili malzemenin ID'si.
○ MalzemeMiktar (float): İlgili tarifte kullanılacak birim malzeme miktarı

Normalizasyon:
Tablolar, veritabanı normalizasyon kurallarına göre tasarlanmalıdır. Aynı malzemeler birden fazla tarifte kullanılabileceği için Tarif-Malzeme İlişkisi tablosu many-to-many ilişkiyi temsil edecektir.

2. Kullanıcı Arayüzü (GUI) Tasarımı
● Uygulamanın ana ekranında tüm tariflerin listelendiği bir alan olmalıdır.
● Menü: Tarif ekleme, güncelleme ve silme işlemleri için menü seçenekleri olmalıdır.
● Tarif Listesi: Tüm tarifler, isimleri, hazırlama süreleri ve maliyet bilgisiyle birlikte ana
ekranda görüntülenmelidir. Herhangi bir tarife tıklandığında tarif detayları
gösterilmelidir.
● Arama ve Filtreleme Alanı: Uygulamanın üst kısmında bulunacak bu alandan tarif arama
ve filtreleme işlemleri kullanıcı tarafından yapılabilmelidir.
● Sonuç Ekranı: Farklı kriterdeki aramalara göre yapılabilecek yemekler listelenmelidir.

3. Fonksiyonel Özellikler Tarif Ekleme
● Kullanıcı, tarifin adını, kategorisini, hazırlama süresini ve tarifin yapılış talimatlarını ekler.
● Her tarifte birden fazla malzeme bulunabilir ve bu malzemeler miktarıyla birlikte ayrı ayrı girilecektir. Birim malzeme veritabanında önceden kaydedilmiş ise kullanıcı tarife malzeme eklerken seçenek olarak seçip ekleyecek ancak kayıtlı değil ise o anda “yeni
malzeme ekle” butonuyla veritabanına birim malzeme ve bilgilerini kaydedecektir.
● Kullanıcı arayüzü üzerinde "Tarif Ekle" butonuna bastığında, tarif ve malzeme bilgileri
veritabanına kaydedilecektir.

 Tarif Önerisi
● Tüm tarifler içerisinden yetersiz veya eksik malzemeli tarifler kırmızıya, yapılabilmesi için tüm malzemesi yeterli miktarda olan tarifler ise yeşile boyanarak kullanıcıya gösterilmelidir.
● Kırmızıya boyanan tariflerde tarifin yapılabilmesi için eksik malzemelerin toplam maliyeti hesaplanarak ayrıca gösterilmelidir.
● Tarif Önerisi fonksiyonu tüm arama ve filtreleme işlemlerinde aktif olarak kullanılmalıdır.

Dinamik Arama
● Arama çubuğu aracılığıyla tarif adı veya malzemeye göre arama yapılabilir. - Tarif Adına Göre Arama
● Kullanıcı arayüzdeki arama alanından tarfi ismine göre tarif aratarak uygun tarifler listelenmelidir.
- Malzemeye Göre Arama
● Kullanıcı arayüzünde bir malzeme girdi alanı olacaktır.
● Kullanıcı mevcut malzemeleri seçtikten sonra, sistem veritabanındaki tarifleri kontrol
etmeli
● Girdi malzemeler, sistemdeki kayıtlı tariflerin malzemeleriyle karşılaştırılarak bir
eşleşme yüzdesi hesaplanmalı ve eşleşme yüzdesine göre en yüksek olandan düşük
olana doğru tarifler listelenmelidir.
● Listeleme sırasında tarifler eşleşme yüzdesiyle birlikte gösterilmelidir.
Filtreleme ve Sıralama
● Kullanıcı, tarifleri hazırlama süresine göre (en hızlıdan en yavaşa veya tersi), tarif maliyetine göre (Artan-Azalan), sıralayabilir .
● Ayrıca malzeme sayısına, kategoriye veya maliyet aralığına göre filtreleme seçenekleri sunulacaktır.
Tarif Güncelleme ve Silme
● Kullanıcı, daha önce eklenen tarifleri güncelleyebilir veya silebilir.
● Tarif güncellendiğinde veya silindiğinde veritabanı otomatik olarak güncellenir.

Duplicate Kontrolü
● Aynı tarifin birden fazla kez kaydedilmesini önlemek için veritabanında duplicate kontrolü yapılacaktır.
Uyarılar:
● Proje sunumlarına gelmeden önce en az 5 farklı kategori ve her bir kategoriye ait en az 10 tarif veritabanına kaydedilmiş bir şekilde hazır bulunmalıdır.
● Kullanıcı arayüzü basit, sezgisel ve kullanıcı dostu olmalı, kullanıcıların hedeflerine minimum çabayla ulaşmasını sağlamalıdır. Ayrıca tutarlı bir tasarım dili ile görsel hiyerarşi, netlik ve işlevsellik ön planda tutulmalıdır.
