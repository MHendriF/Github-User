# Github User

Aplikasi ini dibuat untuk memenuhi syarat kelulusan pada kelas Dicoding [Belajar Fundamental Aplikasi Android](https://www.dicoding.com/academies/14)

### Fitur yang ada pada aplikasi

1. Favorite User
    * Syarat:
		* Aplikasi harus bisa **menambah** dan **menghapus** user dari daftar favorite.
		* Aplikasi harus mempunyai halaman yang menampilkan daftar favorite.
		* Menampilkan halaman detail dari daftar favorite.
		
2. Reminder
    * Syarat: 
		* Terdapat pengaturan untuk menghidupkan dan mematikan reminder di halaman Setting.
		* Daily reminder untuk kembali ke aplikasi yang berjalan pada pukul 09.00 AM.
		
3. Consumer App
    * Syarat:
		* Membuat module baru yang menampilkan daftar user favorite.
		* Menggunakan Content Provider sebagai mekanisme untuk mengakses data dari satu aplikasi ke aplikasi lain.

4. Stack Widget untuk menampilkan daftar user favorite (opsional).

### Additional
    
    * Menggunakan library pihak ketiga seperti Retrofit, Fast Android Networking, dsb.
	* Menggunakan library penyimpanan lokal pihak ketiga seperti Room, Realm, dsb.
    * Menerapkan design pattern seperti MVP, MVVM, Arch Component, dsb.
    * Aplikasi bisa memberikan pesan eror jika data tidak berhasil ditampilkan.
    * Menuliskan kode dengan bersih.

### Resources
	* Untuk menambah limit request di Github API silakan ikuti tutorial pada tautan berikut:
		https://www.dicoding.com/blog/apa-itu-rate-limit-pada-github-api/
	* Gunakan endpoint berikut untuk mendapatkan data:
		Search : https://api.github.com/search/users?q={username}
		Detail user : https://api.github.com/users/{username}
		Follower : https://api.github.com/users/{username}/followers
		Following : https://api.github.com/users/{username}/following
		
### Author
_M Hendri Febriansyah_