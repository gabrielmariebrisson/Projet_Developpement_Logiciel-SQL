<script setup lang="ts">

import { ref, computed } from 'vue';
import router from "@/router";
import { api } from '@/http-api';
import { ImageType } from '@/image'




    const compteur = ref(1);
    const miseRecuperee = ref(false);
    const coinsWin = ref(0);
    const monId = parseInt(sessionStorage.getItem('monId')!);

    async function  incrementer() {
      if (compteur.value === 0) {
        return;
      }

      const random = Math.floor(Math.random() * 4);
      if (random === 3) {
        compteur.value = 0;
      } else {
        compteur.value *= 2;
      }
    }

    async function  recupererMise() 
    {
      if (miseRecuperee.value || compteur.value === 0) {
        return;
      }

      // TODO: ajouter le nombre de coins correspondant dans une variable coinsWin
      coinsWin.value = compteur.value;
      const coins = coinsWin.value;
      
      api.winCoins(monId, coins);

      // TODO: enregistrer dans le sessionStorage que la mise a été récupérée
      const currentTime = new Date().getTime();
      sessionStorage.setItem(`lastMiseRecuperee${monId}`, currentTime.toString());
      const mesAnciensCoins = parseInt(sessionStorage.getItem('mescoins')!);
      if(mesAnciensCoins == null)
      {
        sessionStorage.setItem("mesCoins", "" + coins);
      }
      else
      {
        sessionStorage.setItem("mesCoins", "" + coins + mesAnciensCoins);
      }
     

      miseRecuperee.value = true;
    }

    // Vérifier si la mise a déjà été récupérée dans l'heure
    const lastMiseRecuperee = parseInt(sessionStorage.getItem(`lastMiseRecuperee${monId}`) ?? '0');
    if (lastMiseRecuperee > 0) {
      const currentTimestamp = new Date().getTime();
      const heureMs = 3600 * 1000;
      const tempsEcoule = currentTimestamp - lastMiseRecuperee;
      miseRecuperee.value = tempsEcoule < heureMs;
    }

</script>


<template>
  <div>
    <h1>Bouton chanceux</h1>
    <p>Compteur : {{ compteur }}</p>
    <button @click="incrementer()">Cliquer pour tenter de doubler le compteur (1 chance sur 4 de tout perdre)</button>
    <br> <br>
    <button @click="recupererMise()" :disabled="miseRecuperee || compteur === 0">Récupérer la mise</button>
    <p v-if="miseRecuperee">Vous avez récupéré {{ coinsWin }} coins</p>
  </div>
</template>

<!-- <style scoped src="@/styles.css"></style>
<style>
  .button {
    display: inline-block;
    background-color: #4CAF50;
    border: none;
    color: white;
    padding: 10px 20px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    margin: 4px 2px;
    cursor: pointer;
    border-radius: 4px;
  }

  .button-disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }

  h1 {
    font-size: 36px;
    font-weight: bold;
    margin-bottom: 20px;
  }

  p {
    font-size: 20px;
    margin-bottom: 10px;
  }

  .container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 5px;
  }
</style> -->