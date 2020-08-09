

// 税前计算税后  mode 0:工资  1:劳务
export function toPureIncome(value, mode=0){
  if(isNaN(value)) return 0;
  if(value<=0) return 0;
  let taxes = [
    [//工资
      [3500, 5000, 8000, 12500, 38500, 58500, 83500, value],
      [0,       0,    0,     0,     0,     0,     0,     0],
      [3500, 3500, 3500,  3500,  3500,  3500,  3500,  3500],
      [0,    0.03, 0.10,  0.20,  0.25,  0.30,  0.35,  0.45],
      [0,       0,  105,   555,  1005,  2755,  5505, 13505]
    ],
    [//劳务
      [800, 4000, 20000, 50000, value],
      [  0, 0,      0.2,   0.2,   0.2],
      [  0, 800,      0,     0,     0],
      [  0, 0.2,    0.2,   0.3,   0.4],
      [  0, 0,        0,  2000,  7000]
    ]
  ];
  let levels = taxes[mode];
  let index = levels[0].findIndex((v) => {return v>=value;});
  let result = Number.parseFloat(value - (value * (1 - levels[1][index]) - levels[2][index]) * levels[3][index] + levels[4][index]).toFixed(2);
  return result;
}

// 税后计算税前  mode 0:工资  1:劳务
export function toSellingPrice(value, mode=0) {
  if(isNaN(value)) return 0;
  if(value<=0) return 0;

  let taxes = [
    [//工资
      [3500, 4955, 7655, 11255, 30755, 44755, 61005, value],
      [0,       0,    0,     0,     0,     0,     0,     0],
      [3500, 3500, 3500,  3500,  3500,  3500,  3500,  3500],
      [0,    0.03, 0.10,  0.20,  0.25,  0.30,  0.35,  0.45],
      [0,       0,  105,   555,  1005,  2755,  5505, 13505]
    ],
    [//劳务
      [800, 3360, 16000, 37000, value],
      [  0,    0,   0.2,   0.2,   0.2],
      [  0,  800,     0,     0,     0],
      [  0,  0.2,   0.2,   0.3,   0.4],
      [  0,    0,     0,  2000,  7000]
    ]
  ];
  let levels = taxes[mode];
  let index = levels[0].findIndex((v) => {return v>=value;});
  let result = Number.parseFloat((value - levels[4][index] - levels[2][index] * levels[3][index]) / ( 1 - levels[3][index] + levels[1][index] * levels[3][index])).toFixed(2);
  return result;
}
