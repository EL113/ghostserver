export function range (start, end, strip) {
  const array = []
  while (start <= end) {
    array.push(start)
    start += strip
  }
  return array
}

export default {
  range
}
