<script setup lang="ts">

  import { defineProps, ref, watchEffect } from 'vue';
  import {computed } from 'vue';
  import router from "@/router";
  import { api } from '@/http-api';
  import { ImageType } from '@/image'
  import { onMounted } from 'vue';
  import internal from 'stream';




  interface Statut 
  {
    idStatut: number;
    nameStatut: string
  }

  interface User 
  {
    iduser: number;
    pseudo: string;
    password: string;
    mail: string;
    birthday: Date;
    dateInscription: string;
    coins: number;
    autorisation: number;
    id: number;
    statut: Statut;
  }

  const user = ref<User | null>(null);

  async function login() 
  {
    
    try 
    {
      const mail = (document.getElementById("mail") as HTMLInputElement).value;
      const password = (document.getElementById("password") as HTMLInputElement).value;

      user.value  = await api.postLogin(mail, password);

      console.log(`doit s'afficher ici :`);
      
      if (user.value) 
      {

        const monId = user.value.id;
        const monPseudo = user.value.pseudo;
        const monPassword = user.value.password;
        const monMail = user.value.mail;
        const mesCoins = user.value.coins;
        const maDateInscription = user.value.dateInscription;
        const monAutorisation = user.value.autorisation;
        const monStatut = user.value.statut.nameStatut;
    
        

        const birthday = new Date(user.value.birthday);
        const monBirthday = birthday.toISOString();

        //sessionStorage.setItem("monId", monId.toString());
        sessionStorage.setItem("monId", "" + monId);
        sessionStorage.setItem("monPseudo", monPseudo);
        sessionStorage.setItem("monPassword", monPassword);
        sessionStorage.setItem("monMail", monMail);  
        sessionStorage.setItem("monBirthday", monBirthday);
        sessionStorage.setItem("mesCoins", "" + mesCoins);
        sessionStorage.setItem("maDateInscription", maDateInscription); 
        sessionStorage.setItem("monAutorisation", "" + monAutorisation);
        sessionStorage.setItem("monStatut", "" + monStatut);

      }

    }
    catch (error)
    {
      console.error(error);
    }

    sessionStorage.setItem("needReload", "true");
    router.push({ name: 'home'});

  }

  var monId = sessionStorage.getItem('monId');
   
  const isLoggedIn = computed(() => !!monId);
  
</script>


<template>
  <div>
    <h1>Login</h1>
    <!-- <p>Bonjour {{ pseudo }}!</p> -->
    <template v-if="!isLoggedIn">

      <h2> Se connecter :  </h2>
      
      <form method="post" action="/login" >
        <div> 
          <label for="mail">Mail:</label>
          <input type="text" id="mail" name="mail">
        </div>
        <br>
        <div>
          <label for="password">Password:</label>
          <input type="password" id="password" name="password">
        </div>
        <div>  
          <button @click.prevent="login()" class="button button-primary">Login</button>
        </div>
      </form>
    </template>
 
  
    <template v-else>
        <h1> Vous semblez être déja connecté (si ce n'est pas le cas retourner sur la pagee d'accueil et appuyer sur F5)</h1>

    </template>

  </div>
</template>

<style scoped src="@/styles.css"></style>