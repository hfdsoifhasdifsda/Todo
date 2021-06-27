<template>
  <div class="container">
    <header class="jumbotron">
      <h3>Todo</h3>
    </header>
    <Form @submit="post" :validation-schema="schema">
      <div class="form-group">
        <label for="">題名</label>
        <Field name="subject" type="text" class="form-control" />
        <ErrorMessage name="subject" class="error-feedback" />
      </div>

      <div class="form-group">
        <button class="btn btn-primary btn-block" :disabled="loading">
          <span
            v-show="loading"
            class="spinner-border spinner-border-sm"
          ></span>
          <span>投稿</span>
        </button>
      </div>

      <div class="form-group">
        <div v-if="message" class="alert alert-danger" role="alert">
          {{ message }}
        </div>
      </div>
    </Form>
    <Form @submit="deletePost" :validation-schema="cehckSchema">
      <div>
        <div class="form-group" v-for="(todo, i) in todos" :key="i">
          <input
            :id="'todo' + i"
            type="checkbox"
            :value="todo"
            v-model="selectedTodos"
          >
          <label :for="'todo' + i">{{todo.message}}</label>
        </div>
      </div>
      <div class="form-group">
        <button class="btn btn-primary btn-block" :disabled="loading">
          <span
            v-show="loading"
            class="spinner-border spinner-border-sm"
          ></span>
          <span>削除</span>
        </button>
      </div>
    </Form>
  </div>
</template>

<script>
import TodoService from "../services/todo.service";
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";

export default {
  name: "Profile",
  components: {
    Form,
    Field,
    ErrorMessage,
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
  },
  data() {
    const schema = yup.object().shape({
      message: yup.string(),
    });
    const checkSchema = yup.object().shape({
      message: yup.string(),
    });
    return {
      todos: [],
      selectedTodos: [],
      subject: "",
      schema,
      checkSchema,
    };
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push("/login");
    }
    TodoService.get().then(
      (response) => {
        this.todos = response.data;
      },
      (error) => {
        this.todos =
          (error.response &&
            error.response.data &&
            error.response.data.message) ||
          error.message ||
          error.toString();
      }
    );
  },
  methods: {
    post(data) {
      TodoService.post(data).then(
        (response) => {
          this.todos = response.data;
          this.$router.go({
            path: this.$router.currentRoute.path,
            force: true,
          });
        },
        (error) => {
          this.message =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();
        }
      );
    },
    deletePost() {
      TodoService.delete(this.selectedTodos).then(
        (response) => {
          console.log(this.selectedTodos);
          this.todos = response.data;
          this.$router.go({
            path: this.$router.currentRoute.path,
            force: true,
          });
        },
        (error) => {
          this.message =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();
        }
      );
    },
  },
};
</script>