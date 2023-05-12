<script setup lang="ts">
import { defineProps, ref } from 'vue';
import { api } from '@/http-api';
import router from "@/router";


const errorMessage = ref<string>('');



let brightness2 = 0;
let contrast2 = 0;

/*
 const checkboxInput: HTMLInputElement = document.getElementById('brightness') as HTMLInputElement;
 const checkboxInput2: HTMLInputElement = document.getElementById('contrast') as HTMLInputElement;
 if (checkboxInput.checked) {
   brightness2 = 1;
  
 }
 if (checkboxInput2.checked) {
   contrast2 = 1;

 } */



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

  function modifieImage_Histogram_equalization() 
  {



    // const numberInput1 = document.getElementById("dataValue1") as HTMLInputElement;
    // const inputValue_1 = parseInt(numberInput1.value, 10); // 10 indique que nous utilisons la base 10
    // const numberInput2 = document.getElementById("dataValue1") as HTMLInputElement;
    // const inputValue_2 = parseInt(numberInput2.value, 10); // 10 indique que nous utilisons la base 10
    api.getAlgorithme_p1_p2(props.id, "Histogram_equalization", brightness2, contrast2)
    .then((data: Blob) => {
      lecture_data(data);
      if(want_picture){
          telechargement_image(data,props.id+"Histogram_equalization")
        }
    })
    .catch(e => {
        e.response.data.text().then((text: any) => {
          console.log(text);
          errorMessage.value = `Error ${e.response.status}: ${text}`;
        });      
      });
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


  function modifyBrightness(){
    if(brightness2==0){
      brightness2=1;
    }else{
      brightness2=0;
    }
  }

  function modifContrast(){
    if(contrast2==0){
      contrast2=1;
    }else{
      contrast2=0;
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
      <!-- <button @click="modifyWantPicture()" class="button button-primary">Télécharger l'image filtrée ON/OFF</button> -->

      On: Télchargé l'image :
      <div class="switch">
        <label>
          <input @click="modifyWantPicture()" type="checkbox" />
          <span class="slider"></span>
        </label>
        <span class="label">public</span>
      </div>
      
      <div class="input-group">

        numberInput1

        <!-- <input type="number" id="dataValue1"  placeholder="Luminosité" min="0" max="250" required>
        <input type="number" id="dataValue2"  placeholder="Luminosité" min="0" max="250" required> -->
        <label>


          <input @click="modifyBrightness()" type="checkbox" id = "brightness" class="button"> Luminosité
        </label>
        <label>
          <input @click="modifContrast()" type="checkbox" id = "contrast" class="button"> Contraste
        </label>


        <button @click="modifieImage_Histogram_equalization()" class="button button-primary">Appliquer</button>
      </div>
      <div v-if="errorMessage" class="error">
        {{ errorMessage }}
      </div>
    </div>
  </div>
</template>

<style scoped src="@/styles.css"></style>