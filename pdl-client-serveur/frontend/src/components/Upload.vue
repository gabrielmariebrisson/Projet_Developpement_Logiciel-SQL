<script setup lang="ts">
import { api } from '@/http-api';
import { ref } from 'vue';

const target = ref<HTMLInputElement>();
const errorMessage = ref<string>('');
var checkboxInput=0;
sessionStorage.setItem('clé', 'valeur');
var monId = sessionStorage.getItem('monId');

function submitFile() {var IDuser: number = 8;
  if (target.value !== null && target.value !== undefined && target.value.files !== null && monId!=null) {
    const file = target.value.files[0];
    const IDuser=parseInt(monId);
    if (file === undefined)
      return;
    let formData = new FormData();
    formData.append("file", file);
    api.createImage(formData,IDuser,checkboxInput).then(() => {
      if (target.value !== undefined)
        target.value.value = '';
      errorMessage.value='';
      alert("File uploaded successfully!");
      }).catch(e => {
        console.log(e.response.data);
        errorMessage.value = `Error ${e.response.status}: ${e.response.data}`;
      });
  }
}

function handleFileUpload(event: Event) {
  target.value = (event.target as HTMLInputElement);
}

function modifierCheckbox(){
  if(checkboxInput==0){
    checkboxInput=1;
    return ;
  }checkboxInput=0;
  return ;
}
</script>

<template>
  <head>
  <link rel="stylesheet" href="../stylesheet.css">
</head>
  <div>
    <h3>Upload an image</h3>
    <div>
      <input type="file" id="file" ref="file" @change="handleFileUpload" />
    </div>
    <div>
      <button @click="submitFile" class="button">Submit</button>
    </div>
    <div v-if="errorMessage" class="error">
      {{ errorMessage }}
    </div>
     On: Public / Off : Private 
    <div class="switch">
      <label>
        <input @click="modifierCheckbox()" type="checkbox" />
        <span class="slider"></span>
      </label>
      <span class="label">public</span>
    </div>

  </div>
</template>


<style scoped src="@/styles.css"></style>