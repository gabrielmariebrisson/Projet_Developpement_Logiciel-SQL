<script setup lang="ts">

import router from "@/router";

    sessionStorage.removeItem("monId");
    sessionStorage.removeItem("monPseudo");
    sessionStorage.removeItem("monPassword");
    sessionStorage.removeItem("monMail");
    sessionStorage.removeItem("monBirthday");
    sessionStorage.removeItem("mesCoins");
    
    sessionStorage.setItem("needReload", "true");
    router.push({ name: 'home'});

</script>

<style scoped src="@/styles.css"></style>