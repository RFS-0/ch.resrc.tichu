/// <reference types="vite/client" />
declare module "*.vue" {
    import Vue from 'vue';
    export default Vue;
    import { defineComponent } from "vue";
    const component: ReturnType<typeof defineComponent>;
    export default component;
}
