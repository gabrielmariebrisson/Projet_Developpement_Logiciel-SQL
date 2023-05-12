<script setup lang="ts">
import { defineProps } from 'vue';
import { api } from '@/http-api';
import router from "@/router";
import { ref } from 'vue';

const props = defineProps<{ id: number }>()
const filterSelected = ref('');
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
        console.log(e.response.data);
        errorMessage.value = `Error ${e.response.status}: ${e.response.data}`;
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

          


          imgElt.classList.add("img-uniforme");

          imgElt.style.maxWidth = "500px";
          imgElt.style.maxHeight = "500px";
          MyGalleryElt.appendChild(imgElt);
          if (imgElt !== null && reader.result as string) {
            imgElt.setAttribute("src", (reader.result as string));
          }
        }
      };
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


  function apply(id2: number, filter : string) {

    console.log(filter);
    console.log(id2);
    router.push({ name: filter,  params: { id: props.id  }  })

    }

</script>



<template>
  <table>
    <tr>
      <td><figure :id="'MyGallery-'+id" ></figure></td>
      <td>
        <br><br><br><br>
        <button @click="deleteFile" class="button button-danger">Supprimer</button> 
        <br><br>
        <button @click="downloadImage()" class="button button-primary" >Télécharger l'image</button>
        <br><br>
        <select name="filter" id="filterSelected" v-model="filterSelected" >
          <option value="">-- Select a filter --</option>
          <option value="filterIncreaseLuminosity"> Augmenté la luminosité</option>
          <option value="filterSobel">Filtre Sobel</option>
          <option value="filterHistogramEqualize"> Egaliser l'Histogramme</option>
          <option value="filterColorize">Colorier</option>
          <option value="filterFlou">Flouter</option>  
         </select>
        <button @click="apply(props.id, filterSelected)" class="button">Appliquer le filtre</button><br>
        <div v-if="errorMessage" class="error">
          {{ errorMessage }}
        </div>
        
    </td>
    </tr>
  </table>
</template>


<style scoped src="@/styles.css"></style>