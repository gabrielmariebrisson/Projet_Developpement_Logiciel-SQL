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



const get3RichestUsers = async () => {
 try {
      users.value = await api.getUsersCoins();
   } catch (error) {
     console.error(error);
   }
};

get3RichestUsers();



async function checkUser(idUser: number){
  
  console.log("iduser : " + idUser);
  router.push({ name: 'checkProfil', params: { idU: idUser } })


}



</script>

 <template>
 <div v-if="users.length > 0">
    <h2>Liste des utilisateurs lol:</h2>
    <center>

      <table class="user-table">
        <thead>
          <tr>
            <th>Pseudo</th>
            <th>Nombre de coins</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(user, index) in users" :key="index">
  
            <td>{{ user.pseudo }}</td>
            <td>{{ user.coins }}</td>
            <td v-if="user.statut.idStatut == 1">
            <button @click.prevent="checkUser(user.id)" class="button button-blue">Voir son profil</button>
          </td>
          <td v-else>
            Le profil est privé
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

