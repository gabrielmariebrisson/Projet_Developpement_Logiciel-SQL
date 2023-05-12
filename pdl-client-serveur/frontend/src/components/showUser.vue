<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { api } from '@/http-api';
import internal from 'stream';
import router from "@/router";


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


const users = ref<User[]>([]);



const getAllUsers = async () => {
  try {
    users.value = await api.getAllUsers();
  } catch (error) {
    console.error(error);
  }
};

getAllUsers();



async function deleteUser(idUser: number){
  await api.deleteUser(idUser);
  sessionStorage.setItem("needReload", "true");

  router.push({ name: 'home'});
  // Mettre à jour la liste des utilisateurs après la suppression
 
}
async function checkUser(idUser: number){
  
  console.log("iduser : " + idUser);
  router.push({ name: 'checkProfil', params: { idU: idUser } })


}


const filteredUsers = () => {
  return users.value.filter(user => user.statut.idStatut !== 2);
};
async function GoToDeleted()
{
  router.push({ name: 'showDeletedUser'});
}

</script>

<template>
  <div v-if="users.length > 0">
    <h2>Liste des utilisateurs lol:</h2>
    <center>
      <br>
      <button @click.prevent="GoToDeleted()" class="button button-light-red">Voir les utilisateurs Supprimer</button>
      <br>
      <table class="user-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Pseudo</th>
            <th>Adresse mail</th>
            <th>Date de naissance</th>
            <th>Nombre de coins</th>
            <th>Statut</th>
            <th>Id Statut</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(user, index) in filteredUsers()" :key="index">  
  
            <td>{{ user.id }}</td>
            <td>{{ user.pseudo }}</td>
            <td>{{ user.mail }}</td>
            <td>{{ user.birthday }}</td>
            <td>{{ user.coins }}</td>
            <td>{{ user.statut.nameStatut }}</td>
            <td>{{ user.statut.idStatut }}</td>
            <td>
              <button @click.prevent="deleteUser(user.id)" class="button button-red">Supprimer</button>
              <br>
              <button @click.prevent="checkUser(user.id)" class="button button-blue">Voir le profil</button>
            </td>
          </tr>
        </tbody>
      </table>
    </center>
  </div>
  <div v-else>
    <p>Pas d'utilisateurs trouvés</p>
  </div>
</template>

 <style scoped src="@/styles.css"> </style>