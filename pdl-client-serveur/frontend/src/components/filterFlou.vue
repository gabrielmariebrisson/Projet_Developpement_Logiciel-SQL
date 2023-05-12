<script setup lang="ts">
import { defineProps, ref } from 'vue';
import { api } from '@/http-api';
import router from "@/router";

const props = defineProps<{ id: number }>()
const errorMessage = ref<string>('');

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
      errorMessage.value='';
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
          MyGalleryElt.appendChild(imgElt);
          if (imgElt !== null && reader.result as string) {
            imgElt.setAttribute("src", (reader.result as string));
          }
        }
      };
  }

  let want_picture = false;


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


  function modifieImage_meanFilterSimple() 
  {

    const numberInput1 = document.getElementById("dataValue1") as HTMLInputElement;
    const inputValue_1 = parseInt(numberInput1.value, 10); // 10 indique que nous utilisons la base 10

    const BorderType_id = document.getElementById("BorderType") as HTMLInputElement;
    const BorderType = parseInt(BorderType_id.value, 10); // 10 indique que nous utilisons la base 10

      api.getAlgorithme_p1_p2(props.id, "meanFilterSimple", inputValue_1, BorderType)
      .then((data: Blob) => {
        lecture_data(data);
        if(want_picture){
          telechargement_image(data,props.id+"meanFilterSimple")
        }
        errorMessage.value='';
      }).catch(e => {
        e.response.data.text().then((text: any) => {
          console.log(text);
          errorMessage.value = `Error ${e.response.status}: ${text}`;
        });  
      });
  }



</script>



<template>
  <table>
    <tr>
      <td><figure :id="'MyGallery-'+id"></figure></td>
      <td>
        <br><br><br><br>
        <button @click="deleteFile">Supprimer</button> 
        <br><br>
        <!-- <button @click="modifyWantPicture()">Télécharger l'image filtrée ON/OFF</button> -->

        On: Télchargé l'image :
      <div class="switch">
        <label>
          <input @click="modifyWantPicture()" type="checkbox" />
          <span class="slider"></span>
        </label>
        <span class="label">public</span>
      </div>

        <br><br><br>
        <input type="number" id="dataValue1"  placeholder = "value of flou" required>
        <br><br>
        
        <select type="number"  id="BorderType">
            <option value="">-- Select a border --</option>
            <option value="0">SKIP</option>
            <option value="1">EXTENDED</option>
            <option value="2">REFLECT</option>
            <option value="3">NORMALIZED</option>
        </select>
        <p>1 paramètre floute l'image 1 paramètre max la taille de l'image</p>
        <button @click="modifieImage_meanFilterSimple()">Floutage</button><br>
        <div v-if="errorMessage" class="error">
          {{ errorMessage }}
        </div>
      </td>
    </tr>
  </table>
</template>

<style scoped src="@/styles.css"></style>