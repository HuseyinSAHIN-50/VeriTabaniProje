// --- Sekme Geçiş Efektleri ---
var x = document.getElementById("login");
var y = document.getElementById("register");
var z = document.getElementById("btn");
var f = document.getElementById("forgot"); // YENİ: Şifre formu

function registerKisim() {
    x.style.left = "-400px";
    y.style.left = "50px";
    f.style.left = "450px"; // Şifre formunu sağa at
    z.style.left = "110px";
}

function loginKisim() {
    x.style.left = "50px";
    y.style.left = "450px";
    f.style.left = "450px"; // Şifre formunu sağa at
    z.style.left = "0px";
    
    // Formu sıfırla (Belki geri dönmüştür)
    document.getElementById("step1").style.display = "block";
    document.getElementById("step2").style.display = "none";
    document.getElementById("resetMessage").innerText = "";
}

function sifremiUnuttumAc() {
    x.style.left = "-400px"; // Login gizle
    y.style.left = "450px";  // Register gizle
    z.style.left = "0px";    // Buton efekti (Login tarafında kalsın)
    f.style.left = "50px";   // Forgot formunu sahneye al
}

// --- API İletişimi ---

// 1. GİRİŞ YAP (LOGIN)
async function loginYap() {
    const email = document.getElementById("loginEmail").value;
    const password = document.getElementById("loginPassword").value;
    const messageBox = document.getElementById("loginMessage");

    messageBox.innerText = "Giriş yapılıyor...";

    try {
        const response = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email: email, password: password })
        });

        if (response.ok) {
            const token = await response.text(); 
            localStorage.setItem("jwtToken", token); 
            localStorage.setItem("userEmail", email); 
            
            messageBox.style.color = "green";
            messageBox.innerText = "Başarılı! Yönlendiriliyorsunuz...";
            
            setTimeout(() => {
                window.location.href = "panel.html"; 
            }, 1000);
        } else {
            messageBox.style.color = "red";
            messageBox.innerText = "Hata: Kullanıcı adı veya şifre yanlış.";
        }
    } catch (error) {
        console.error(error);
        messageBox.innerText = "Sunucu hatası!";
    }
}

// 2. KAYIT OL (REGISTER)
async function kayitOl() {
    const data = {
        firstName: document.getElementById("regName").value,
        lastName: document.getElementById("regSurname").value,
        email: document.getElementById("regEmail").value,
        phone: document.getElementById("regPhone").value,
        password: document.getElementById("regPassword").value
    };

    const messageBox = document.getElementById("regMessage");

    try {
        const response = await fetch("http://localhost:8080/api/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            const result = await response.text();
            messageBox.style.color = "green";
            messageBox.innerText = result; 
            setTimeout(loginKisim, 2000); 
        } else {
            messageBox.style.color = "red";
            messageBox.innerText = "Kayıt Başarısız! (Mail kullanılıyor olabilir)";
        }
    } catch (error) {
        console.error(error);
        messageBox.innerText = "Sunucuya bağlanılamadı.";
    }
}

// 3. ŞİFRE SIFIRLAMA - KOD GÖNDER
async function kodGonder() {
    const email = document.getElementById("resetEmail").value;
    const msg = document.getElementById("resetMessage");

    if(!email) { msg.innerText = "Lütfen e-posta giriniz."; return; }
    msg.style.color = "#666";
    msg.innerText = "Kod gönderiliyor, lütfen bekleyin...";

    try {
        const res = await fetch("http://localhost:8080/api/auth/forgot-password", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email: email })
        });

        if(res.ok) {
            msg.style.color = "green";
            msg.innerText = "Kod mailinize gönderildi!";
            // Geçiş Efekti
            document.getElementById("step1").style.display = "none";
            document.getElementById("step2").style.display = "block";
        } else {
            msg.style.color = "red";
            msg.innerText = "Bu e-posta sistemde kayıtlı değil.";
        }
    } catch(e) { 
        msg.style.color = "red";
        msg.innerText = "Hata oluştu."; 
    }
}

// 4. ŞİFRE SIFIRLAMA - YENİ ŞİFRE BELİRLE
async function sifreyiDegistir() {
    const email = document.getElementById("resetEmail").value;
    const code = document.getElementById("resetCode").value;
    const newPass = document.getElementById("newPass").value;
    const msg = document.getElementById("resetMessage");

    if(!code || !newPass) { msg.innerText = "Tüm alanları doldurun."; return; }

    try {
        const res = await fetch("http://localhost:8080/api/auth/reset-password", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email: email, code: code, newPassword: newPass })
        });

        if(res.ok) {
            alert("Şifreniz başarıyla değiştirildi! Yeni şifrenizle giriş yapabilirsiniz.");
            loginKisim(); // Giriş ekranına at
        } else {
            msg.style.color = "red";
            msg.innerText = "Kod hatalı veya süresi dolmuş.";
        }
    } catch(e) { 
        msg.style.color = "red";
        msg.innerText = "İşlem başarısız."; 
    }
}