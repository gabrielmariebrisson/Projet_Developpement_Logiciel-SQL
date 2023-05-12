<script setup lang="ts">
import { ref } from 'vue';
import router from "@/router";
import { api } from '@/http-api';
import { ImageType } from '@/image'


const errorMessage = ref<string>('');





const selectedId = ref(-1);
const filterSelected = ref('');
const imageList = ref<ImageType[]>([]);
const selectedImage = ref<Blob|null>(null);
getImageList();

interface User {
  iduser: number;
  pseudo: string;
  password: string;
  mail: string;
  birthday: Date;
  dateInscription: string;
  coins: number;
  autorisation: number;
  id: number;
}

async function loginUser() {
  const mail = (document.getElementById("mail") as HTMLInputElement).value;
  const password = (document.getElementById("password") as HTMLInputElement).value;
  console.log(`User mail: ${mail}`);
  console.log(`User password: ${password}`);
  const user = ref<User | null>(null);
  user.value = await api.postLogin(mail, password);
  if(user.value!=null){
    console.log(`User user.value.id: ${user.value.id}`);
    return user.value.id;
  }return 8;
}
sessionStorage.setItem('clé', 'valeur');
var monId = sessionStorage.getItem('monId');


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


api.getImage(selectedId.value)
    .then((data: Blob) => 
    {
      selectedImage.value = data;
    })
    .catch(e => {
      e.response.data.text().then((text: any) => {
          console.log(text);
          errorMessage.value = `Error ${e.response.status}: ${text}`;
        });  
    });


function apply(id2: number, filter : string) {

  console.log(filter);
  console.log(id2);
  router.push({ name: filter,  params: { id: selectedId.value  }  })
  
}

function getImageUrl() {
  return selectedImage.value ? window.URL.createObjectURL(selectedImage.value) : '';
}

</script>




<template>
  <div>
    <h3>Choose an image</h3>
  </div>
  <div>


    <select v-model="selectedId"  class="button button-primary">
      <option v-for="image in imageList" :value="image.id" :key="image.id">{{ image.name }}</option>
      
    </select>

    <select name="filter" id="filterSelected" v-model="filterSelected"  class="button button-primary" >
   
    <option value="">-- Select a filter --</option>
    <option value="filterIncreaseLuminosity"> Augmenté la luminosité</option>
    <option value="filterSobel">Filtre Sobel</option>
    <option value="filterHistogramEqualize"> Egaliser l'Histogramme</option>
    <option value="filterColorize">Colorier</option>
    <option value="filterFlou">Flouter</option>
    </select>


    <button @click="apply(selectedId, filterSelected)" class="button">Aller au filtre</button><br>


    <div v-if="selectedImage">
            <img :src="getImageUrl()" />
    </div>
    <div v-if="errorMessage" class="error">
      {{ errorMessage }}
    </div>  
  </div>

 

</template>

<style scoped src="@/styles.css"></style>