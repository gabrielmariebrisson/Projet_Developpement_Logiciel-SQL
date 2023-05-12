<script setup lang="ts">
import { defineProps, ref } from 'vue';
import { api } from '@/http-api';
import router from "@/router";

const props = defineProps<{ id: number }>()
const errorMessage = ref<string>('');
let want_picture = false;


  api.getImage(props.id)
    .then((data: Blob) => 
    {
      lecture_data(data);
      errorMessage.value='';
      }).catch(e => {
        e.response.data.text().then((text: any) => {
          console.log(text);
          errorMessage.value = `Error ${e.response.status}: ${text}`;
        });  
      });



  
  function deleteFile() 
  {
    api.deleteImage(props.id).then(() => {
    }).catch(e => {
      console.log(e.message);
    });
    router.push({ name: 'home'});
    //getImageList();
  }




  function lecture_data(data: Blob){
    const reader = new window.FileReader();
      reader.readAsDataURL(data);
      reader.onload = () => {
        const galleryElt = document.getElementById("MyGallery-"+props.id);
        if (galleryElt !== null) {
          const imgElt = document.createElement("img");
          galleryElt.appendChild(imgElt);
          if (imgElt !== null && reader.result as string) {
            imgElt.setAttribute("src", (reader.result as string));
          }
        }
      };
  }




  function modifieImage_sobel()
  {

    api.getAlgorithme(props.id, "gradientImageSobel")
    .then((data: Blob) => {
      lecture_data(data);
      if(want_picture){
        telechargement_image(data, "thresholdBrightness");
      }
      errorMessage.value='';
    })
    .catch(e => {
      e.response.data.text().then((text: any) => {
          console.log(text);
          errorMessage.value = `Error ${e.response.status}: ${text}`;
        });      
    });
  }




  function downloadImage() 
  {
    const image = document.querySelector(`#MyGallery-${props.id} img`) as HTMLImageElement;
    const link = document.createElement("a");
    link.href = image.src;
    link.download = "image.jpg";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
  function telechargement_image(data: Blob,name :string) {
    const link = document.createElement("a");
    const reader = new window.FileReader();

    reader.readAsDataURL(data);
    
    reader.onload = () => {
      link.href = reader.result as string;
      link.download = name;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    };
  }

  function modifyWantPicture(){
    if(want_picture){
      want_picture=false;
    }else{
      want_picture=true;
    }
  }

</script>


<template>
  <div class="container">
    <div class="MyGallery">
      <figure :id="'MyGallery-'+id"></figure>
    </div>
    <div class="actions">
      <button @click="deleteFile" class="button button-danger">Supprimer</button> 
      <!-- <button @click="modifyWantPicture()">Télécharger l'image filtré On/Off </button> -->
    </div>
    

      On: Télchargé l'image :
      <div class="switch">
        <label>
          <input @click="modifyWantPicture()" type="checkbox" />
          <span class="slider"></span>
        </label>
        <span class="label">public</span>
      </div>

      <div class="input-group">
        <button @click="modifieImage_sobel()" class="button button-primary">Appliquer</button>
      </div>
      <div v-if="errorMessage" class="error">
        {{ errorMessage }}
      </div>
    </div>
 
</template>


<style scoped src="@/styles.css"></style>