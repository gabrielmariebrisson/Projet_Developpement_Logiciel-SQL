<script setup lang="ts">
import { defineProps, ref, watchEffect } from 'vue';
import { api } from '@/http-api';
import router from "@/router";
import { ImageType } from '@/image'
import Image from './Image.vue';

import { SourceTextModule } from 'vm';

//id = idUser du profil

// const imageList = ref<ImageType[]>([]);
// const filterSelected = ref('');
// const errorMessage = ref<string>('');

const props = defineProps<{ idU: number }>()
const errorMessage = ref<string>('');

interface Statut 
{
  idStatut: number;
  nameStatut: string
}

interface User 
{
  iduser: number;
  pseudo: string;
  password: string;
  mail: string;
  birthday: Date;
  dateInscription: string;
  coins: number; 
  autorisation: number;
  id: number;
  statut: Statut;
}



interface ImageClement
{
  idImage: number;
  name: string;

  dateCreation: string;
  path: string;

  user: User;
  statutI: Statut;

  idImg : number;
}



//const users = ref<User[]>([]);
const images = ref<ImageClement[]>([]);

console.log("idUser 1 = " + props.idU);

const filteredImages = async () => {
 try {
    var idUser = props.idU;
    console.log("idUser 2 = " + idUser);

    //users.value = await api.getAllUsers();
    images.value = await api.selectImageWithIdUser(idUser);

   } 
   catch (error)
   {
      console.error(error);
   }
};

filteredImages();

// get3RichestUsers();

// async function filteredImages()
// {
//   var idUser = props.id;

//   images.value = await api.selectImageWithIdUser(idUser);
// }






watchEffect(() => {
  images.value.forEach((image) => {
    if (image.statutI.idStatut !== 2) {
      displayImage(image.idImg);
    }
  });
});



const displayImage = async (idImg: number) => {
  try {
    const data: Blob = await api.getImage(idImg);
    lecture_data(data, idImg);
    errorMessage.value = '';
  } catch (error) {
    console.error(error);
  }
};




/*
function lecture_data(data: Blob, idImg: number) {
  const reader = new window.FileReader();
  reader.readAsDataURL(data);
  reader.onload = () => {
    const galleryElt = document.getElementById("gallery-" + idImg);
    if (galleryElt !== null) {
      const imgElt = document.createElement("img");
      galleryElt.appendChild(imgElt);
      if (imgElt !== null && reader.result as string) {
        imgElt.setAttribute("src", reader.result as string);
      }
    }
  };
} */


 function lecture_data(data: Blob, idImg: number) {
   const reader = new FileReader();
   reader.readAsDataURL(data);
   reader.onload = () => {
     const galleryElt = document.getElementById("gallery-" + idImg);
     if (galleryElt !== null) {
       const imgElt = document.createElement("img");
       imgElt.src = reader.result as string;
      imgElt.onload = () => {
        if (imgElt.width > 700) {
           imgElt.width = 700;
         }
        if (imgElt.height > 700) {
           imgElt.height = 700;
        }
         galleryElt.appendChild(imgElt);
       };
     }
   };
 }

/*

api.getImage(idImg: number)
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
        const galleryElt = document.getElementById("gallery-"+images.idImg);
        if (galleryElt !== null) {
          const imgElt = document.createElement("img");
          galleryElt.appendChild(imgElt);
          if (imgElt !== null && reader.result as string) {
            imgElt.setAttribute("src", (reader.result as string));
          }
        }
      };
  } */




</script>




<template>
  <!-- <div v-if="images.length > 0"> -->
     <div>
     <h2>Profil du user : {{ props.idU }}</h2>
     <center>
 
       <table class="user-table">
         <thead>
           <tr>
            <th> ID </th>
             <th>Name</th>
             <th>path</th>
             <th>Image Data</th>
           </tr>
         </thead>
         <tbody>
          <tr v-for="(image, index) in images" :key="index">
            <td v-if="image.statutI.idStatut < 2"> {{ image.idImg }}</td>
            <td v-if="image.statutI.idStatut < 2">{{ image.name }}</td>
            <td v-if="image.statutI.idStatut < 2">{{ image.path }}</td>
            <td>
              <figure :id="'gallery-' + image.idImg" v-if="image.statutI.idStatut < 2"></figure>
            </td>
          </tr>

         </tbody>
       </table>
     </center>
   </div> 
   <!-- <div v-else>
     <p>Pas d'images trouvés</p>
   </div> -->
 </template> 


<style scoped src="@/styles.css"></style>