<script setup lang="ts">
import { defineProps, ref } from 'vue';
import { api } from '@/http-api';
import router from "@/router";

const errorMessage = ref<string>('');
const props = defineProps<{ id: number }>()
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
        const MyGalleryElt = document.getElementById("MyGallery-"+props.id);
        if (MyGalleryElt !== null) {
          const imgElt = document.createElement("img");
          MyGalleryElt.appendChild(imgElt);
          if (imgElt !== null && reader.result as string) {
            imgElt.setAttribute("src", (reader.result as string));
          }
        }
      };
  }

  function modifieImage_thresholdBrightness() 
  {

    const numberInput1 = document.getElementById("dataValue1") as HTMLInputElement;
    const inputValue_1 = parseInt(numberInput1.value, 10); // 10 indique que nous utilisons la base 10
    api.getAlgorithme_p1(props.id, "thresholdBrightness", inputValue_1)
    .then((data: Blob) => {
      lecture_data(data);
      if(want_picture){
        telechargement_image(data, "thresholdBrightness");
      }errorMessage.value='';
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
      <!-- <button @click="modifyWantPicture()" class="button button-primary">Télécharger l'image filtrée</button> -->


      On: Télchargé l'image :
      <div class="switch">
        <label>
          <input @click="modifyWantPicture()" type="checkbox" />
          <span class="slider"></span>
        </label>
        <span class="label">public</span>
      </div>


      <button @click="downloadImage()" class="button">Télécharger l'image</button>
      <div class="input-group">
        <input type="number" id="dataValue1"  placeholder="Luminosité" min="0" max="250" required>
        <button @click="modifieImage_thresholdBrightness()" class="button button-primary">Appliquer</button>
      </div>
      <div v-if="errorMessage" class="error">
        {{ errorMessage }}
      </div>
    </div>
  </div>
</template>

<style scoped src="@/styles.css"></style>