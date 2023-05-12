<script setup lang="ts">
import { defineProps, ref, watchEffect } from 'vue';
import {computed } from 'vue';
import router from "@/router";
import { api } from '@/http-api';
import { ImageType } from '@/image'
import { onMounted } from 'vue';
import internal from 'stream';



const errorMessage = ref<string>('');


interface Statut {
    idStatut: number;
   nameStatut: string
}

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


const user = ref<User | null>(null);

//const user2 = ref<User | null>(null);



async function login() 
{
  
  try 
  {
    //Récupération des valeur du formulaire de connexion
    const mail = (document.getElementById("mail") as HTMLInputElement).value;
    const password = (document.getElementById("password") as HTMLInputElement).value;

    user.value  = await api.postLogin(mail, password);
    
    if (user.value) 
    {

      const monId = user.value.id;
      const monPseudo = user.value.pseudo;
      const monPassword = user.value.password;
      const monMail = user.value.mail;
      const mesCoins = user.value.coins;
      const maDateInscription = user.value.dateInscription;
      const monAutorisation = user.value.autorisation;
      const monStatut = user.value.statut.nameStatut;
      
    
      //conversion de type Date vers string
      const birthday = new Date(user.value.birthday);
      const monBirthday = birthday.toISOString();

      
      //sessionStorage.setItem("monId", monId.toString());      
      sessionStorage.setItem("monId", "" + monId);
      sessionStorage.setItem("monPseudo", monPseudo);
      sessionStorage.setItem("monPassword", monPassword);
      sessionStorage.setItem("monMail", monMail);  
      sessionStorage.setItem("monBirthday", monBirthday);
      sessionStorage.setItem("mesCoins", "" + mesCoins);
      sessionStorage.setItem("maDateInscription", maDateInscription); 
      sessionStorage.setItem("monAutorisation", "" + monAutorisation);    
      sessionStorage.setItem("monStatut", monStatut); 
      sessionStorage.setItem("monStatut2", "public"); 

    }

  } 
  catch (error) 
  {
    console.error(error);
  }

  sessionStorage.setItem("needReload", "true");
  router.push({ name: 'home'});
}

  //Récupération des variables de session
var monId = parseInt(sessionStorage.getItem('monId')!);
var monPseudo = sessionStorage.getItem('monPseudo');

var monPassword = sessionStorage.getItem('monPassword');
var monMail = sessionStorage.getItem('monMail');
var monBirthday = sessionStorage.getItem('monBirthday');
var mesCoins = sessionStorage.getItem('mesCoins');
var maDateInscription = sessionStorage.getItem('maDateInscription');
var monAutorisation = sessionStorage.getItem('monAutorisation');
var monStatut = sessionStorage.getItem('monStatut');

const monBirthdayFormated = new Date(sessionStorage.getItem('monBirthday')!).toISOString().substr(0, 10);




//Stocke l'état de connexion du user
const isLoggedIn = computed(() => !!monId);



async function modifier() 
{

    //Récupération des variables dans le formulaire de modification de profil
    const pseudo2 = (document.getElementById("pseudo2") as HTMLInputElement).value;
    const password2 = (document.getElementById("password2") as HTMLInputElement).value;
    const mail2 = (document.getElementById("mail2") as HTMLInputElement).value;
    const birthdayString2 = (document.getElementById("birthday2") as HTMLInputElement).value;
    const statut2 = (document.getElementById("statut2") as HTMLInputElement).value;

    // Conversion de string en date
    const birthday2 = new Date(birthdayString2);
    
    // Récupération d'un nouveau user avec les modifications appliqués
    api.modifyUser(pseudo2, password2, mail2, birthday2, statut2, monId);
    
  

          
      //suppression des anciennes informations
      sessionStorage.removeItem("monPseudo");
      sessionStorage.removeItem("monPassword");
      sessionStorage.removeItem("monMail");
      sessionStorage.removeItem("monBirthday");
      sessionStorage.removeItem("monStatut");

        
     
      const monBirthday = birthday2.toISOString();

      //Modification des nouvelle variables sessions
      sessionStorage.setItem("monPseudo", pseudo2);
      sessionStorage.setItem("monPassword", password2);
      sessionStorage.setItem("monMail", mail2);  
      sessionStorage.setItem("monBirthday", monBirthday);
      sessionStorage.setItem("monStatut", statut2);

      sessionStorage.setItem("needReload", "true");

      router.push({ name: 'home'});
}







const images = ref<ImageClement[]>([]);

console.log("idUser 1 = " + monId);

const filteredImages = async () => {
 try {
    var idUser = monId;
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


 function modifierStatutImage(ImgId: number, statutId: number)
 {
   if(statutId == 0)
   {
    statutId++;
   }
   if(statutId == 1)
   {
    statutId--;
   }

  api.changeImgStatut(ImgId, statutId);
  sessionStorage.setItem("needReload", "true");

      router.push({ name: 'home'});

 }

 function deleteImage(ImgId: number, statutId: number)
 {
  api.changeImgStatut(ImgId, 2);
  sessionStorage.setItem("needReload", "true");
  router.push({ name: 'home'});
 }

</script>


<template>
  <div>
    <h1>Login</h1>
    
    <template v-if="!isLoggedIn">
      <h1> Vous semblez ne pas être connecté :( </h1> <br>
      <h2> Veuillez vous connecter :  </h2>
      <center>
      <form method="post" action="/login" >
        <div> 
          <label for="mail">Mail:</label>
          <input type="text" id="mail" name="mail">
        </div>
        <div>
          <label for="password">Password:</label>
          <input type="password" id="password" name="password">
        </div>
        <div>  
          <button @click.prevent="login()" class="button button-primary">Login</button>
        </div>
      </form>
    </center>
    </template>
 
  
      <template v-else>
        <h2>Bonjour {{ monPseudo }}!</h2>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Pseudo</th>
          <th>Adresse mail</th>
          <th>Mot de passe</th>
          <th>Date de naissance</th>
          <th>Nombre de coins</th>
          <th>Statut</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>{{ monId }}</td>
          <td><input type="text" id="pseudo2" name="pseudo2" v-model="monPseudo"></td>
          <td><input type="mail" id="mail2" name="mail2" v-model="monMail"></td>
          <td><input type="text" id="password2" name="password2" v-model="monPassword"></td>
          <td><input type="date"  id="birthday2" name="birthday2" v-model="monBirthdayFormated"></td>
                   
          <td>{{ mesCoins }}</td>
          <td>
            <select v-model="monStatut"  id="statut2" name="statut2">
              <option value="public">Public</option>
              <option value="private">Private</option>
            </select>
          </td>
        </tr>
      </tbody>
    </table>
    <button @click="modifier()" class="button button-primary">Modifier le profil</button>


    <center>
      <br><br>
      <h2>Mes Images</h2>
      <br><br>
 <table class="user-table">
   <thead>
     <tr>
      <th> ID </th>
       <th>Name</th>
       <th>Image </th>
       <th>Statut</th>
     </tr>
   </thead>
   <tbody>
    <tr v-for="(image, index) in images" :key="index" >
      <td v-if="image.statutI.idStatut !== 2"> {{ image.idImg }}</td>
      <td v-if="image.statutI.idStatut !== 2">{{ image.name }}</td>
      <td v-if="image.statutI.idStatut !== 2">
      <figure :id="'gallery-' + image.idImg" v-if="image.statutI.idStatut !== 2"></figure>
      </td>
      <td v-if="image.statutI.idStatut !== 2">{{ image.statutI.nameStatut }}
        <button @click="modifierStatutImage(image.idImg, image.statutI.idStatut)" class="button button-primary">Changer le statut</button>
        <button @click="deleteImage(image.idImg, image.statutI.idStatut)" class="button button-red">Supprimer</button>
      </td>
    </tr>

   </tbody>
 </table>
</center>
  </template>


  </div>
</template>

<style scoped src="@/styles.css"></style>