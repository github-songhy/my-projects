<template>
  <transition name="el-zoom-in-top" @after-leave="doDestroy">
    <div
        v-show="showPopper"
        :class="{ 'is-loading': !parent.hideLoading && parent.loading }"
        :style="{ width: dropdownWidth }"
        class="el-autocomplete-suggestion el-popper"
        role="region">
      <el-scrollbar
          tag="ul"
          view-class="el-autocomplete-suggestion__list"
          wrap-class="el-autocomplete-suggestion__wrap">
        <li v-if="!parent.hideLoading && parent.loading"><i class="el-icon-loading"></i></li>
        <slot v-else>
        </slot>
      </el-scrollbar>
    </div>
  </transition>
</template>
<script>
import Popper from 'element-ui/src/utils/vue-popper';
import Emitter from 'element-ui/src/mixins/emitter';
import ElScrollbar from 'element-ui/packages/scrollbar';

export default {
  components: {ElScrollbar},
  mixins: [Popper, Emitter],

  componentName: 'ElAutocompleteSuggestions',

  data() {
    return {
      parent: this.$parent,
      dropdownWidth: ''
    };
  },

  props: {
    options: {
      default() {
        return {
          gpuAcceleration: false
        };
      }
    },
    id: String
  },

  methods: {
    select(item) {
      this.dispatch('ElAutocomplete', 'item-click', item);
    }
  },

  updated() {
    this.$nextTick(_ => {
      this.popperJS && this.updatePopper();
    });
  },

  mounted() {
    this.$parent.popperElm = this.popperElm = this.$el;
    this.referenceElm = this.$parent.$refs.input.$refs.input || this.$parent.$refs.input.$refs.textarea;
    this.referenceList = this.$el.querySelector('.el-autocomplete-suggestion__list');
    this.referenceList.setAttribute('role', 'listbox');
    this.referenceList.setAttribute('id', this.id);
  },

  created() {
    this.$on('visible', (val, inputWidth) => {
      this.dropdownWidth = inputWidth + 'px';
      this.showPopper = val;
    });
  }
};
</script>
