package main

import "math/rand"

func inStringSlice(el interface{}, arr []string) bool {
	for v := range arr {
		if el == v {
			return true
		}
	}
	return false
}

func stringMapKeys(m map[string]int32) []string {
	keys := make([]string, 0, len(m))
	for k := range m {
		keys = append(keys, k)
	}

	return keys
}

func randomValFromStringSlice(s []string) string {
	return s[rand.Intn(len(s))]
}

func randomEnumValFromMap(m map[string]int32) int32 {
	keys := stringMapKeys(m)
	return m[randomValFromStringSlice(keys)]
}
