<script setup lang="ts">
import { computed } from 'vue';

var monId = sessionStorage.getItem('monId');
  var monPseudo = sessionStorage.getItem('monPseudo');

  var monAutorisation = parseInt(sessionStorage.getItem('monAutorisation')!);
  
  const isLoggedIn = computed(() => !!monId);

</script>

<template>
  <div id="app">
    <nav>
      <ul class="button">
        <li>
          <router-link to="/">Home</router-link>
        </li>
        <li>
          <router-link to="/PublicGallery">Public Gallery</router-link>
        </li>
        <li v-if="isLoggedIn">
          <router-link to="/MyGallery">My Gallery</router-link>
        </li>
        <li v-if="isLoggedIn">
          <router-link to="/upload">Upload</router-link>
        </li>
        <li v-if="isLoggedIn">
          <router-link to="/WinCoins">Gagner des Coins</router-link>
        </li>

        <li>
          <router-link to="/chooseFilter">Choose Filter</router-link>
        </li>
        <!-- Afficher "Mon profil" si l'utilisateur est connecté -->
        <li v-if="isLoggedIn" style="float: right;">
          <router-link to="/myProfile">{{ monPseudo }}</router-link>
        </li>
        <li v-else style="float: right;">
          <router-link to="/register">Register</router-link>
        </li>
        
        <!-- Afficher "Connexion" si l'utilisateur n'est pas connecté -->
        <li v-if="isLoggedIn" style="float: right;">
          <router-link to="/deconexion"> Deconexion </router-link>
        </li>
        <li v-else style="float: right;">
          <router-link to="/loginPage">Connexion</router-link>
        </li>
       
        <li v-if="monAutorisation > 1">
          <router-link to="/showUser">User</router-link>
        </li>
        <li>
          <router-link to="/ranking">Classements</router-link>
        </li>
        
      </ul>
    </nav>

    <div>
      <h1>PDL - L3</h1>
      <router-view />
    </div>
  </div>
</template>

<style >
#app {
  text-align: center;
  font-family: Arial, Helvetica, sans-serif;
  color: #2c3e50;
}
ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  overflow: hidden;
  background-color: #333;
}
li {
  float: left;
}
li a {
  display: block;
  color: white;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
}
.error {
  color: red;
  font-weight: bold;
  list-style-type: none;
  margin-top: 1em;
}
</style>
