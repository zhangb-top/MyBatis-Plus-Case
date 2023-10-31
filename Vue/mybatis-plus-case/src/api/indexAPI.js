import request from '@/util/request'

export const codeGeneratorAPI = (codeGeneratorDTO) => {
  return request.post('/main/code-generator', codeGeneratorDTO)
}