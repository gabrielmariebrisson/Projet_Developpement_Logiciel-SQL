<script setup lang="ts">
import { ref } from 'vue'
import { api } from '@/http-api';
import { ImageType } from '@/image'
import Image from './ImagePublic.vue';
import { SourceTextModule } from 'vm';

const imageList = ref<ImageType[]>([]);
const errorMessage = ref<string>('');

sessionStorage.setItem('clé', 'valeur');
var monId = sessionStorage.getItem('monId');

getImageListPublic();
async function getImageListPublic() {
  var IDuser: number = 8;
  if (monId!=null){
    IDuser=parseInt(monId);
  }
  api.getImageListPublic().then((data) => {
    imageList.value = data;
    errorMessage.value='';
      }).catch(e => {
        console.log(e.response.data);
        errorMessage.value = `Error ${e.response.status}: ${e.response.data}`;
      });
}

</script>

<template>
  <div>
    <h3>Public Gallery</h3>
    <Image v-for="image in imageList" :id="image.id" />
  </div>
</template>


<style scoped src="@/styles.css"></style>

