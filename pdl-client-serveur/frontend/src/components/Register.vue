<script setup lang="ts">
import { ref } from 'vue';
import router from "@/router";
import { api } from '@/http-api';
import { ImageType } from '@/image'


const errorMessage = ref<string>('');






function register() 
  {

    const pseudo = (document.getElementById("pseudo") as HTMLInputElement).value;
    const password = (document.getElementById("password") as HTMLInputElement).value;
    const mail = (document.getElementById("mail") as HTMLInputElement).value;
    const birthdayString = (document.getElementById("birthday") as HTMLInputElement).value;
    
    const birthday = new Date(birthdayString);
    api.postUser(pseudo, password, mail, birthday).then(() => {
      errorMessage.value='';
      alert("you are now registered!");
      }).catch(e => {
        console.log(e.response.data);
        errorMessage.value = `Error ${e.response.status}: ${e.response.data}`;
      });
    router.push({ name: 'home'});


  }


</script>

<template>

  
    <h1>Register</h1>

  
    <label for="pseudo">Pseudo:</label><br>
    <input type="text" id="pseudo" name="pseudo"><br><br>

    <label for="password">Password:</label><br>
    <input type="password" id="password" name="password"><br><br>

    <label for="mail">Email:</label><br>
    <input type="mail" id="mail" name="mail"><br><br>

    <label for="birthday">Date de naissance:</label><br>
    <input type="date" id="birthday" name="birthday"><br><br>

    
    <!-- <input type="submit" value="Submit"> -->
    <button @click="register()" class="button button-primary">Enregistrer</button>




</template>


<style scoped src="@/styles.css"></style>