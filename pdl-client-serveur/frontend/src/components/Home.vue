<script setup lang="ts">
import { ref, computed } from 'vue';
import router from "@/router";
import { api } from '@/http-api';
import { ImageType } from '@/image'


// Vérifie si la page abesoin d'être rechargée
  if(sessionStorage.getItem("needReload") == "true") 
  {
    console.log("Rechargement ...");
    sessionStorage.setItem("needReload", "false")
    window.location.reload();
  }

    // var pseudo = sessionStorage.getItem("pseudo");
    // var pseudoElement = document.getElementById("pseudo");
    // if (pseudo != null && pseudoElement != null) {
    //     pseudoElement.innerHTML = pseudo;
    // }

const selectedId = ref(-1);
const imageList = ref<ImageType[]>([]);
const errorMessage = ref<string>('');
function showImage() {
  router.push({ name: 'image', params: { id: selectedId.value } })
}

// var length = sessionStorage.length;
//   console.log("La taille de session storage est : " + length);
// var pseudo = sessionStorage.getItem("pseudo");
//   var machaine = sessionStorage.getItem("chaine");
//     if (machaine !== null) {
//   console.log("Le pseudo est stocké dans la sessionStorage : " + pseudo);
// } else {
//   console.log("Le pseudo n'est pas stocké dans la sessionStorage.");
// }
// console.log("ceci doit s'afficher");


// // Enregistrer des données dans sessionStorage
sessionStorage.setItem('clé', 'valeur');

// // Récupérer des données depuis sessionStorage
var data = sessionStorage.getItem('clé');
console.log("data : " + data);



var monId = sessionStorage.getItem('monId');
  var monPseudo = sessionStorage.getItem('monPseudo');
  var monMail = sessionStorage.getItem('monMail');
  var monPassword = sessionStorage.getItem('monPassword');
  var monBirthday = sessionStorage.getItem('monBirthday');
  
  const isLoggedIn = computed(() => !!monId);
  getImageList();


async function getImageList() {
  var IDuser: number = 8;
  if (monId!=null){
    IDuser=parseInt(monId);
  }
  api.getImageList(IDuser).then((data) => {
    imageList.value = data;
    errorMessage.value='';
      }).catch(e => {
        console.log(e.response.data);
        errorMessage.value = `Error ${e.response.status}: ${e.response.data}`;
      });
}


</script>

<template>
  <!-- <p>Bonjour <span id="pseudo"></span>!</p> -->
  <!-- <p>Bonjour {{ pseudo }}!</p> -->
  <div>
    <h3>Choose an image</h3>
  </div>
  <div>
    <select v-model="selectedId" @change="showImage" class="button">
      <option v-for="image in imageList" :value="image.id" :key="image.id">{{ image.name }}</option>
    </select>
  </div>
  



</template>

<style scoped src="@/styles.css"></style>