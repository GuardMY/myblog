<template>
  <div class="rich-editor">
    <div ref="editor" class="editor"></div>
    <button @click="saveContent" class="save-btn">保存内容</button>
  </div>
</template>

<script>
import EditorJS from '@editorjs/editorjs'
import Header from '@editorjs/header'
import Image from '@editorjs/image'
import Paragraph from '@editorjs/paragraph'
import List from '@editorjs/list'
import Code from '@editorjs/code'
import Link from '@editorjs/link'
import InlineCode from '@editorjs/inline-code'

export default {
  name: 'RichEditor',
  props: {
    initialData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      editor: null
    }
  },
  mounted() {
    this.initEditor()
  },
  beforeUnmount() {
    if (this.editor) {
      this.editor.destroy()
    }
  },
  methods: {
    initEditor() {
      this.editor = new EditorJS({
        holder: this.$refs.editor,
        tools: {
          header: {
            class: Header,
            config: {
              placeholder: '输入标题...',
              levels: [1, 2, 3, 4, 5, 6],
              defaultLevel: 2
            }
          },
          paragraph: {
            class: Paragraph,
            config: {
              placeholder: '输入内容...'
            }
          },
          list: {
            class: List,
            config: {
              defaultStyle: 'unordered'
            }
          },
          code: Code,
          link: Link,
          inlineCode: InlineCode,
          image: {
            class: Image,
            config: {
              uploader: {
                uploadByFile: async (file) => {
                  try {
                    const formData = new FormData()
                    formData.append('file', file)
                    
                    const response = await fetch('http://localhost:8080/api/blogs/upload', {
                      method: 'POST',
                      body: formData
                    })
                    
                    const data = await response.json()
                    return {
                      success: 1,
                      file: {
                        url: data.url
                      }
                    }
                  } catch (error) {
                    console.error('Error uploading image:', error)
                    return {
                      success: 0,
                      message: '上传失败'
                    }
                  }
                },
                uploadByUrl: async (url) => {
                  return {
                    success: 1,
                    file: {
                      url
                    }
                  }
                }
              }
            }
          },
          video: {
            class: {
              toolbox: {
                title: '视频',
                icon: '<svg width="17" height="15" viewBox="0 0 17 15" xmlns="http://www.w3.org/2000/svg"><path d="M16.5 4.5l-15 6.5 15 6.5V4.5z" fill="#000" fill-opacity="0.54"/><path d="M16.5 4.5l-15 6.5 15 6.5V4.5z" fill="#000" fill-opacity="0.54" transform="translate(0 1)"/></svg>'
              },
              render() {
                return {
                  wrapper: null,
                  render() {
                    const wrapper = document.createElement('div')
                    wrapper.className = 'video-block'
                    
                    const input = document.createElement('input')
                    input.type = 'text'
                    input.placeholder = '输入视频链接...'
                    input.className = 'video-input'
                    
                    const preview = document.createElement('div')
                    preview.className = 'video-preview'
                    
                    input.addEventListener('input', (e) => {
                      const url = e.target.value
                      if (url) {
                        preview.innerHTML = `<iframe width="100%" height="315" src="${url}" frameborder="0" allowfullscreen></iframe>`
                      } else {
                        preview.innerHTML = ''
                      }
                    })
                    
                    wrapper.appendChild(input)
                    wrapper.appendChild(preview)
                    this.wrapper = wrapper
                    return wrapper
                  },
                  save() {
                    const input = this.wrapper.querySelector('input')
                    return {
                      url: input.value
                    }
                  },
                  validate() {
                    const input = this.wrapper.querySelector('input')
                    return !!input.value
                  }
                }
              }
            }
          }
        },
        data: this.initialData,
        placeholder: '开始编辑你的内容...'
      })
    },
    async saveContent() {
      try {
        const data = await this.editor.save()
        this.$emit('contentSaved', data)
        console.log('Content saved:', data)
      } catch (error) {
        console.error('Error saving content:', error)
      }
    }
  }
}
</script>

<style scoped>
.rich-editor {
  margin-bottom: 2rem;
}

.editor {
  min-height: 400px;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 1rem;
  background-color: white;
}

.save-btn {
  margin-top: 1rem;
  padding: 0.6rem 1.2rem;
  background-color: #667eea;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s;
}

.save-btn:hover {
  background-color: #5a6fd8;
}

/* Editor.js 自定义样式 */
:deep(.ce-block) {
  margin-bottom: 1.5rem;
}

:deep(.ce-header) {
  margin-bottom: 1rem;
}

:deep(.ce-paragraph) {
  line-height: 1.6;
}

:deep(.ce-list) {
  margin-left: 1.5rem;
}

:deep(.ce-code) {
  background-color: #f5f5f5;
  padding: 1rem;
  border-radius: 4px;
  font-family: 'Courier New', Courier, monospace;
}

:deep(.ce-image) {
  margin: 1rem 0;
}

:deep(.ce-image img) {
  max-width: 100%;
  height: auto;
  border-radius: 4px;
}

.video-block {
  margin: 1rem 0;
}

.video-input {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  margin-bottom: 1rem;
}

.video-preview {
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 1rem;
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f9f9f9;
}

.video-preview iframe {
  border: none;
  border-radius: 4px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .editor {
    min-height: 300px;
    padding: 0.8rem;
  }

  .save-btn {
    padding: 0.5rem 1rem;
    font-size: 0.9rem;
  }

  .video-preview {
    min-height: 150px;
    padding: 0.8rem;
  }

  .video-preview iframe {
    height: 200px;
  }
}
</style>