import { defineStore } from 'pinia'

export interface TagView {
  path: string
  name: string
  title: string
}

export const useTagsViewStore = defineStore('tagsView', {
  state: () => ({
    visitedViews: [] as TagView[]
  }),
  actions: {
    addView(view: any) {
      if (this.visitedViews.some(v => v.path === view.path)) return
      this.visitedViews.push(
        Object.assign({}, view, {
          title: view.meta.title || 'no-name'
        })
      )
    },
    delView(view: TagView) {
      for (const [i, v] of this.visitedViews.entries()) {
        if (v.path === view.path) {
          this.visitedViews.splice(i, 1)
          break
        }
      }
    }
  }
})
