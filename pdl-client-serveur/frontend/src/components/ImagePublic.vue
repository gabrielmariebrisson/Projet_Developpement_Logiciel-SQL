<script setup lang="ts">
import { defineProps,computed } from 'vue';
import { api } from '@/http-api';
import router from "@/router";
import { ref,watch } from 'vue';

const props = defineProps<{ id: number }>()
const filterSelected = ref('');
const errorMessage = ref<string>('');


sessionStorage.setItem('clé', 'valeur');
var monId = sessionStorage.getItem('monId');
const isLoggedIn = computed(() => !!monId);




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



  function lecture_data(data: Blob){
    const reader = new window.FileReader();
      reader.readAsDataURL(data);
      reader.onload = () => {
        const ImagePublicElt = document.getElementById("ImagePublic-"+props.id);
        if (ImagePublicElt !== null) {
          const imgElt = document.createElement("img");
          imgElt.classList.add("img-uniforme");
          
          imgElt.style.maxWidth = "500px";
          imgElt.style.maxHeight = "500px";

          ImagePublicElt.appendChild(imgElt);
          if (imgElt !== null && reader.result as string) {
            imgElt.setAttribute("src", (reader.result as string));
          }
        }
      };
  }

let isLiked = ref(false);
var likeCount = ref<string>('');
//likeCount.value =`0 people like it.`;
getLikeCount()
async function getLikeCount() {
  try {
    likeCount.value = `${(await api.getImageLikeCount(props.id)).toString()} people like it.`;
  } catch (error) {
    console.error(error);
    errorMessage.value = `Error  ${error}`;
  }
}



checkIfUserLikedImage()
async function checkIfUserLikedImage(){
  if(monId != null) {
        const IDuser=parseInt(monId);
        api.checkIfUserLikedImage(props.id,IDuser)
        .then((data) =>{
          isLiked.value = data;
          console.log(` isLiked : ${data}`);
          errorMessage.value='';
        }).catch(e => {
          isLiked.value=false
          e.response.data.text().then((text: any) => {
            console.log(text);
            errorMessage.value = `Error ${e.response.status}: ${text}`;
          });  
        });
      }
}


async function toggleLike() {
  console.log(` isLiked.value :${isLiked}`);
  if (isLiked.value) {
    try {
      if(monId != null) {
        const IDuser=parseInt(monId);
        console.log(` IDuser : ${IDuser}`);
        await api.unlikeImage(props.id,IDuser);
        isLiked.value = false;
        getLikeCount()
      }
    } catch (error) {
      console.error(error);
      errorMessage.value = `Error  ${error}`;
    }
  } else {
    try {
      if(monId != null) {
        console.log(` IDuser : ${monId}`);
        const IDuser=parseInt(monId);
        await api.likeImage(props.id,IDuser);
        isLiked.value = true;
        getLikeCount()
      }
    } catch (error) {
      console.error(error);
      errorMessage.value = `Error ${error}`;
    }
  }
}
</script>



<template>
  <table>
    <tr>
      <td><figure :id="'ImagePublic-'+id"></figure></td>
    </tr>
  </table>
  <div v-if="isLoggedIn">
    <button
      @click="toggleLike"
      class="eart-button"
      :class="isLiked ? 'button-unlike' : 'button-danger'"
    >
  like
</button>
  </div>
  <div v-if="likeCount" >
    {{ likeCount }}
  </div>
  <div v-if="errorMessage" class="error">
    {{ errorMessage }}
  </div>
</template>


<style scoped src="@/styles.css"></style>
